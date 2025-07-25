package noemibaglieri.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "requested_trip_date", nullable = false)
    private LocalDate requestedTripDate;

    @Column(name = "special_requests", nullable = false, length = 1000)
    private String specialRequests;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public Booking(LocalDate requestedTripDate, String specialRequests, Employee employee, Trip trip) {
        this.requestedTripDate = requestedTripDate;
        this.specialRequests = specialRequests;
        this.employee = employee;
        this.trip = trip;
    }
}
