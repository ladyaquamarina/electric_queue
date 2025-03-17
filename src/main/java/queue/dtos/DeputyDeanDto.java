package queue.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeputyDeanDto {
    private UUID id;
    private UUID userId;
    private String faculty;
    private Integer course;

    private UserDto user;
}
