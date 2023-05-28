package com.api.service.impl;

import ch.qos.logback.classic.Logger;
import com.api.exception.NoSubscribersException;
import com.api.exception.SubscriberAlreadySubscribedException;
import com.api.model.Subscriber;
import com.api.service.FileService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    private static final Logger log = (Logger) LoggerFactory.getLogger(FileServiceImpl.class);

    private final Path filePath;

    public FileServiceImpl(@Value("${directory.path}") String directoryPath,
                           @Value("${file.name}") String fileName) throws IOException {
        this.filePath = Paths.get(directoryPath, fileName);
    }

    @Override
    public void addSubscriber(Subscriber subscriber) throws SubscriberAlreadySubscribedException, IOException {
        try {
            if (!Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                log.info("File created: {}", filePath);
            }

            List<Subscriber> subscribers = getSubscribers();

            if (subscribers.contains(subscriber)) {
                log.warn("Subscriber with email {} is already subscribed", subscriber.getMail());
                throw new SubscriberAlreadySubscribedException();
            }

            Files.write(filePath, (subscriber.getMail() + "\n").getBytes(), StandardOpenOption.APPEND);
            log.info("New email was added: {}", subscriber.getMail());
        } catch (IOException e) {
            log.error("An error occurred: {}", e.getMessage());
        }
    }

    @Override
    public List<Subscriber> getSubscribers() throws IOException {
        List<Subscriber> subscribers = new ArrayList<>();

        if (Files.exists(filePath)) {
            try {
                log.info("Getting subscribers from file: {}", filePath);
                List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

                lines.forEach(s -> subscribers.add(new Subscriber(s)));

            } catch (IOException e) {
                log.error("Unable to get subscribers from file");

                throw e;
            }
        } else {
            log.warn("No subscribers were found");

            throw new NoSubscribersException();
        }

        return subscribers;
    }

    public Path getFilePath() {
        return filePath;
    }
}
