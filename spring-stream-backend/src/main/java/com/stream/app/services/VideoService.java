package com.stream.app.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.stream.app.entities.Video;


@Service
public interface VideoService {
	
	//get Video by id
	Video get(String videoId);
	
	//save video
	Video save(Video video, MultipartFile file);
	
	//get Video by title
	Video getByTitle(String title);
	
	//get all videos
	List<Video> getAllVideo();

}
