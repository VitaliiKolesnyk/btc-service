package com.api.service.impl;

import com.api.exception.NoSubscribersException;
import com.api.exception.SubscriberAlreadySubscribedException;
import com.api.model.Subscriber;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceImplTest {

    private static final String directoryPath = "src/test/resources";

    private static final String fileName = "subscribers.txt";

    private FileServiceImpl fileService;

    @BeforeEach
    void setUp() throws IOException {
        fileService = new FileServiceImpl(directoryPath, fileName);
    }

    @AfterEach
    void clear() throws IOException {
        if (Files.exists(fileService.getFilePath())) {
            Files.delete(fileService.getFilePath());
        }
    }

    @Test
    @DisplayName("Should add subscriber to the file")
    void shouldAddSubscriberToFile() throws SubscriberAlreadySubscribedException, IOException {
        Subscriber subscriber1 = new Subscriber("test1@example.com");
        Subscriber subscriber2 = new Subscriber("test2@example.com");

        fileService.addSubscriber(subscriber1);
        fileService.addSubscriber(subscriber2);

        assertTrue(Files.exists(fileService.getFilePath()));
        List<String> lines = Files.readAllLines(fileService.getFilePath());
        assertEquals(2, lines.size());
        assertEquals("test1@example.com", lines.get(0));
        assertEquals("test2@example.com", lines.get(1));
    }

    @Test
    @DisplayName("Should throw exception when subscriber already exists")
    void shouldThrowExceptionWhenSubscriberAlreadyExists() throws IOException {
        Subscriber subscriber = new Subscriber("test@example.com");

        Files.write(fileService.getFilePath(), List.of("test@example.com"));

        assertThrows(SubscriberAlreadySubscribedException.class, () -> fileService.addSubscriber(subscriber));
    }

    @Test
    @DisplayName("Should throw NoSubscribersException")
    void shouldThrowNoSubscribersException() {
        assertThrows(NoSubscribersException.class, () -> fileService.getSubscribers());
    }

    @Test
    @DisplayName("Should return list of subscribers from file")
    void shouldReturnListOfSubscribersFromFile() throws IOException {
        Files.write(fileService.getFilePath(), List.of("test1@example.com", "test2@example.com"));

        List<Subscriber> subscribers = fileService.getSubscribers();

        assertNotNull(subscribers);
        assertEquals(2, subscribers.size());
        assertEquals("test1@example.com", subscribers.get(0).getMail());
        assertEquals("test2@example.com", subscribers.get(1).getMail());
    }
}