package ru.ifmo.egalkin.vought.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ifmo.egalkin.vought.model.enums.ApplicationStatus;
import ru.ifmo.egalkin.vought.model.enums.ApplicationType;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by egalkin
 * Date: 13.10.2021
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Application {

    public static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private String rejectReason;

    private LocalDateTime meetingTime;

    @Column(nullable = false)
    private LocalDate updateDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicationType applicationType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private Employee creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aim_id")
    private Employee meetingAimEmployee;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "employee_id")
//    private Employee processor;

    public boolean isPending() {
        return applicationStatus == ApplicationStatus.PENDING;
    }

    public boolean notEmptyRejectReason() {
        return applicationStatus == ApplicationStatus.REJECTED &&
                rejectReason != null &&
                !rejectReason.isEmpty();
    }

    public boolean notEmptyDescription() {
        return description != null && !description.isEmpty();
    }

    public String getMeetingTimeFormatted() {
        return meetingTime.format(FORMATTER);
    }

    public boolean isMeeting() {
        return applicationType == ApplicationType.MEETING;
    }

}
