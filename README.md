# spring-stream-backend


# Video Streaming Backend Application

A basic Spring Boot-based backend for a video streaming application that manages video uploads, metadata, and playback features.

---

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Features](#features)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)

---

## Installation

To set up and run the backend of the video streaming application locally, follow the steps below.

### Prerequisites

- JDK 11 or later
- Maven (for building the project)
- MySQL 5 or later
- Eclipse EE IDE

### Steps to Install

1. Clone the repository:

   git clone https://github.com/subham1679/spring-stream-backend.git
  

2. Navigate to the project directory:
   
   cd spring-stream-backend-backend

3. Build the project using Maven:
   
   mvn clean install
   

4. Start the application:
   
   mvn spring-boot:run

---

## Usage

Once the application is up and running, the backend will be available at `http://localhost:8080`. You can test the following features:

- **Video Uploading**: Upload videos and associated metadata.
- **Video Streaming**: Retrieve video streams via APIs.

### Example API Requests:

#### Upload a video:

POST /api/videos
Content-Type: multipart/form-data
{
  "file": <video file>
}


#### Get video details:

GET /api/videos/{videoId}


---

## Features

The project includes the following features:

- **Video Upload**: Upload and store videos, with metadata (title, description, tags).
- **Video Streaming**: APIs for fetching video streams with support for chunking and adaptive bitrate streaming.
- **Database Integration**: Uses MySQL (or another relational database) for storing user and video metadata.
- **Error Handling**: Consistent error messages and status codes.

---

## API Documentation

This section describes the available endpoints for the backend.

### Video Management

- `POST /api/videos/upload`: Upload a new video.
- `GET /api/videos/{videoId}`: Get video details.
- `GET /api/videos/stream/{videoId}`: Stream a video by its ID.

### Example Responses

- **POST /api/auth/register**
  json
  {
    "message": "User registered successfully"
  }
  

- **GET /api/videos/{videoId}**
  json
  {
    "videoId": "123",
    "title": "Sample Video",
    "description": "A sample video for testing.",
    "contentType": ".mp3"
  }
  

---

## Contributing

We welcome contributions! If you'd like to improve this project, please follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes.
4. Commit your changes (`git commit -am 'Add new feature'`).
5. Push to your branch (`git push origin feature-branch`).
6. Open a pull request.

---

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

---

## Acknowledgments

- Thanks to **Spring Boot** for providing the framework.
- Thanks to **MySQL** for managing our data.
- Inspired by popular video streaming platforms like **YouTube** and **Netflix**.
