package com.stream.app.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.apache.catalina.connector.Response;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stream.app.constants.AppConstants;
import com.stream.app.entities.Video;
import com.stream.app.payload.CustomMessage;
import com.stream.app.services.VideoService;

@RestController
@RequestMapping("/api/video")
@CrossOrigin("*")
public class VideoController {

	private VideoService videoService;

	public VideoController(VideoService videoService) {
		this.videoService = videoService;
	}

	// this api select a single video by id

	@GetMapping("{videoId}")
	public ResponseEntity<Video> getVideoById(@PathVariable String videoId) {
		Video video = videoService.get(videoId);
		return ResponseEntity.ok(video);
	}

	// this api select all the videos

	@GetMapping
	public List<Video> getAllVideos() {
		return videoService.getAllVideo();
	}

	// this api uploads video

	@PostMapping
	public ResponseEntity<?> create(@RequestParam("file") MultipartFile file, @RequestParam("title") String title,
			@RequestParam("description") String description) {
		Video video = new Video();
		video.setTitle(title);
		video.setDescription(description);
		video.setVideoId(UUID.randomUUID().toString());
		Video savedVideo = videoService.save(video, file);
		if (savedVideo != null) {
			return ResponseEntity.status(HttpStatus.OK).body(video);
		}

		else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(CustomMessage.builder().setMessage("Video not uploaded").setSuccess(false).build());
		}
	}

	// this api streams the video

	@GetMapping("/stream/{videoId}")
	public ResponseEntity<Resource> stream(@PathVariable String videoId) {
		
		Video video = videoService.get(videoId);
		String contentType = video.getContentType();
		String filePath = video.getFilePath();

		Resource resource = new FileSystemResource(filePath);

		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);

	}

	// this api stream video in chunks

	@GetMapping("/stream/range/{videoId}")
	public ResponseEntity<Resource> streamVideoRange(@PathVariable String videoId,
			@RequestHeader(value = "Range", required = false) String range) {
		
		System.out.println("range of current chunk is:\t" + range);
		Video video = videoService.get(videoId);
		String filepath = video.getFilePath();
		Resource resource = new FileSystemResource(filepath);
		Path path = Paths.get(video.getFilePath());
		String contentType = video.getContentType();
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		
		long fileLength = path.toFile().length();
		
		if(range==null)
		{
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
		}
		
		//calculate the range of start and end of the data chunk
		long rangeStart;
		long rangeEnd;
		
		String[] ranges = range.replace("bytes=", "").split("-");
		
		rangeStart  = Long.parseLong(ranges[0]);
		
		rangeEnd= rangeStart + AppConstants.CHUNK_SIZE -1; 
		
		if(rangeEnd >= fileLength)
			rangeEnd = fileLength-1;
		
		
//		if(ranges.length>1)
//			rangeEnd = Long.parseLong(ranges[1]);
//		else
//			rangeEnd = fileLength-1;
//		
//		if(rangeEnd>fileLength-1)
//			rangeEnd = fileLength-1;
		
		System.out.println("range start : " + rangeStart);
        System.out.println("range end : " + rangeEnd);
		InputStream inputStream;
		
		try {
			inputStream = Files.newInputStream(path);
			inputStream.skip(rangeStart);
			
			long contentLength = (rangeEnd - rangeStart) + 1;
			
			byte[] data = new byte[(int) contentLength];
			
			int read = inputStream.read(data,0, data.length);
			
			
			org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
			
			headers.add("Content-Range","bytes "+rangeStart+"-"+rangeEnd+"/"+fileLength);
		    headers.add("Custom-Header", "CustomHeaderValue");       // Add a custom header
		    headers.add("Cache-Control","no-cache, no-store, must-revalidate"); // Cache control headers
		    headers.add("Pragma","no-cache");
	        headers.add("X-Content-Type-Options", "nosniff");
		    headers.add("Expires","0");
		    
		    headers.setContentLength(contentLength);

			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
					.headers(headers)
					.contentType(MediaType.parseMediaType(contentType))
					.body(new ByteArrayResource(data));

			
		} catch (IOException e) {
			
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
		
	}

}
