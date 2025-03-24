package queue.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
    private String faculty;
    private Integer course;
    private String group;
    private LocalDateTime startPeriod;
    private LocalDateTime endPeriod;
    private UUID deputyDeanId;
    private NameValueDto purpose;
}
