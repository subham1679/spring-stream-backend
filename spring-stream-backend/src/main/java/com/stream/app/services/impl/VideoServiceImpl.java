package com.stream.app.services.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.stream.app.entities.Video;
import com.stream.app.repository.VideoRepository;
import com.stream.app.services.VideoService;

import jakarta.annotation.PostConstruct;

import java.nio.file.Files;
import java.nio.file.Path;

//import jakarta.persistence.criteria.Path;

@Service
public class VideoServiceImpl implements VideoService {
    @Value("${files.video}")
	String DIR;
	
    
    @Autowired
    private VideoRepository videoRepository;
    
    @PostConstruct
	 public void init()
	 {
		 File file  = new File(DIR);
		 if(!file.exists())
		 {
			 file.mkdir();
			 System.out.println("Folder created");
		 }
		 else
			 System.out.println("Folder already exists");
	 }

    
	@Override
	public Video get(String videoId) {

		Video video = videoRepository.findById(videoId).orElseThrow(() -> new RuntimeException("video not found"));
        return video;
	}
	
	@Override
	public List<Video> getAllVideo() {

		return videoRepository.findAll();
	}
	
	 
	@Override
	public Video save(Video video, MultipartFile file) {

		
		try {
			String filename = file.getOriginalFilename();
			String fileContentType = file.getContentType();
			InputStream inputStream = file.getInputStream();
			
			
			
			String cleanFileName = org.springframework.util.StringUtils.cleanPath(filename);
			String cleanFolder = org.springframework.util.StringUtils.cleanPath(DIR);
		    Path path = Paths.get(cleanFolder,cleanFileName);
		    
		    System.out.println(path);
		    
		    Files.copy(inputStream, path,StandardCopyOption.REPLACE_EXISTING);
		    
		    // video metadata
		    video.setContentType(fileContentType);
		    video.setFilePath(path.toString());
		    
		    return videoRepository.save(video);
		
		} 
		
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public Video getByTitle(String title) {
		return videoRepository.findByTitle(title);
	}

}
