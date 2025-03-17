package queue.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.repositories.StudentRepository;
import queue.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
}
