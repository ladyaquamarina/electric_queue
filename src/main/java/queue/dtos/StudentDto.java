package queue.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private UUID id;
    private UUID userId;
    private String group;
    private Integer course;
    private String faculty;

    private UserDto user;
}
