package com.stream.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.stream.app.entities.Video;

@Service
@Repository
public interface VideoRepository extends JpaRepository<Video, String> {

	Video findByTitle(String title);
	
	Video findByVideoId(String videoId);
}
