package com.moviehouse.service;

import com.moviehouse.dto.MovieDto;
import com.moviehouse.dto.MoviePageResponse;
import com.moviehouse.exceptions.FileExistsException;
import com.moviehouse.exceptions.MovieNotFoundException;
import com.moviehouse.model.Movie;
import com.moviehouse.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService{
    private final MovieRepository movieRepository;
    private final FileService fileService;

    @Value("${project.poster}")
    public String path;

    @Value("${base.url}")
    public String baseURL;

    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        if (Files.exists(Paths.get(path + File.separator+ file.getOriginalFilename()))){
            throw new FileExistsException("File already exists! please enter another file name");
        }

       String uploadedFileName = fileService.uploadFile(path, file);

//       setting the value of field "poster" as filename
       movieDto.setPoster(uploadedFileName);

//       mapping dto to Movie object
       Movie movie = new Movie(
               null,
               movieDto.getTitle(),
               movieDto.getDirector(),
               movieDto.getStudio(),
               movieDto.getMovieCast(),
               movieDto.getReleaseYear(),
               movieDto.getPoster()
       );
//       saving the movie object and returning the saved Movies object
       Movie savedMovie = movieRepository.save(movie);
//      generating the posterUrl
        String posterUrl = baseURL + "/file/" + uploadedFileName;
//        mapping the Movies object to DTO object and returning it
        MovieDto response = new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );


        return response;
    }

    @Override
    public MovieDto getMovie(Integer movieId) {
//         check the data in DB and if it exists, fetch the data of given ID
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id = " + movieId));

        //  generate posterUrl
        String posterUrl = baseURL + "/file/" + movie.getPoster();

        // mapping to MovieDto object and return it
        MovieDto response = new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );

        return response;
    }

    @Override
    public List<MovieDto> getAllMovies() {
//        fetch all data from database
       List<Movie> movies = movieRepository.findAll();

       List<MovieDto> movieDtos = new ArrayList<>();
//        generate posterUrl for each movie object and map to MovieDto object
    for (Movie movie : movies){
        String posterUrl = baseURL + "/file/" + movie.getPoster();
        MovieDto movieDto = new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );
        movieDtos.add(movieDto);

    }
        return movieDtos;
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {

//        check if the movie object exists with given movieId
        Movie mv = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id = " + movieId));
//        if file is null, do nthng
//        if file != null, del existing file connected with the record and upload new file
        String fileName = mv.getPoster();
            if (file!=null){
            Files.deleteIfExists(Paths.get(path + File.separator + fileName));
            fileName = fileService.uploadFile(path, file);
        }
//        set movieDto's poster value
         movieDto.setPoster(fileName);
//        map it to Movie Object
        Movie movie = new Movie(
                mv.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

//        save the movie object -> return saved movie object
        Movie updatedMovie = movieRepository.save(movie);
//        generate posterUrl
        String posterUrl = baseURL + "/file/" + movie.getPoster();
//        map to MovieDto and return it
        MovieDto response = new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );
        return response;
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException {
//        check if the movie object exists in the database
        Movie mv = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id = " + movieId));
        Integer id = mv.getMovieId();
//        deleting the file associated with this object
        Files.deleteIfExists(Paths.get(path + File.separator + mv.getPoster()));
//        deleting the movie object
        movieRepository.delete(mv);

        return "Movie deleted with id = " + id;
    }

    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();

        List<MovieDto> movieDtos = new ArrayList<>();
//        generate posterUrl for each movie object and map to MovieDto object
        for (Movie movie : movies) {
            String posterUrl = baseURL + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);
        }
            return new MoviePageResponse(movieDtos, pageNumber, pageSize, moviePages.getTotalPages(),
                    (int) moviePages.getTotalElements(), moviePages.isLast());



    }

    @Override
    public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
        Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();

        List<MovieDto> movieDtos = new ArrayList<>();
//        generate posterUrl for each movie object and map to MovieDto object
        for (Movie movie : movies) {
            String posterUrl = baseURL + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);
        }
        return new MoviePageResponse(movieDtos, pageNumber, pageSize,
                 moviePages.getTotalElements(),  moviePages.getTotalPages(), moviePages.isLast());
    }
}
