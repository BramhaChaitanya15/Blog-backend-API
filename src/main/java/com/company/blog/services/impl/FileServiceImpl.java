package com.company.blog.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.company.blog.services.FileService;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public String uploadimage(String path, MultipartFile file) throws IOException {
		// file name
		String name = file.getOriginalFilename();
		// random name generated file
		String randomID = UUID.randomUUID().toString();
		String fileName1 = randomID.concat(name.substring(name.lastIndexOf(".")));
		// full path
		String fullPath = path + File.separator + fileName1;
		// create folder if not created
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}
		// file copy
		Files.copy(file.getInputStream(), Paths.get(fullPath));
		return fileName1;
	}

	@Override
	public InputStream getResource(String path, String fileName) throws FileNotFoundException {
		String fullPath = path + File.separator + fileName;
		InputStream is = new FileInputStream(fullPath);
		return is;
	}

	@Override
	public void deleteFile(String path, String fileName) throws IOException {
		String fullPath = path + File.separator + fileName;
		Files.deleteIfExists(Paths.get(fullPath));
	}

	@Override
	public void renameFile(String path, String oldImageName, String newImageName) throws IOException {
		// Construct the full path for the source and destination files
		Path sourcePath = Paths.get(path + File.separator + oldImageName);
		Path targetPath = Paths.get(path + File.separator + newImageName);
		if (Files.exists(sourcePath)) {
			try {
				Files.move(sourcePath, targetPath, StandardCopyOption.ATOMIC_MOVE);
			} catch (IOException e) {
				System.err.println("Failed to rename file: " + oldImageName + " to " + newImageName);
				throw e; // Re throw the exception to be handled by the controller
			}
		} else {
			System.err.println("File not found: " + oldImageName);
		}
	}

}
