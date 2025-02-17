package mk.ukim.finki.wp.kol2024g1.service.Impl;

import mk.ukim.finki.wp.kol2024g1.model.Hotel;
import mk.ukim.finki.wp.kol2024g1.model.Reservation;
import mk.ukim.finki.wp.kol2024g1.model.RoomType;
import mk.ukim.finki.wp.kol2024g1.model.exceptions.InvalidHotelIdException;
import mk.ukim.finki.wp.kol2024g1.model.exceptions.InvalidReservationIdException;
import mk.ukim.finki.wp.kol2024g1.repository.ReservationRepository;
import mk.ukim.finki.wp.kol2024g1.service.HotelService;
import mk.ukim.finki.wp.kol2024g1.service.ReservationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static mk.ukim.finki.wp.kol2024g1.service.specification.FieldFilterSpecification.*;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final HotelService hotelService;

    public ReservationServiceImpl(ReservationRepository reservationRepository, HotelService hotelService) {
        this.reservationRepository = reservationRepository;
        this.hotelService = hotelService;
    }

    @Override
    public List<Reservation> listAll(){
        return reservationRepository.findAll();
    }

    @Override
    public Reservation findById(Long id){
        return reservationRepository.findById(id).orElse(null);
    }

    @Override
    public Reservation create(String guestName, LocalDate dateCreated, Integer daysOfStay, RoomType roomType, Long hotelId){
        return reservationRepository.save(
                new Reservation(guestName, dateCreated, daysOfStay, roomType, hotelService.findById(hotelId)));
    }

    @Override
    public Reservation update(Long id, String guestName, LocalDate dateCreated, Integer daysOfStay, RoomType roomType, Long hotelId){
        Reservation reservation = reservationRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Reservation not found: " + id));
        Hotel hotel = hotelService.findById(hotelId);
        reservation.setGuestName(guestName);
        reservation.setDateCreated(dateCreated);
        reservation.setDaysOfStay(daysOfStay);
        reservation.setRoomType(roomType);
        reservation.setHotel(hotel);
        this.reservationRepository.save(reservation);
        return reservation;
    }

    @Override
    public Reservation delete(Long id){
        Reservation reservation = reservationRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Reservation not found "+id));
        reservationRepository.deleteById(id);
        return reservation;
    }

    @Override
    public Reservation extendStay(Long id){
        Reservation reservation = reservationRepository.findById(id).get();
        reservation.setDaysOfStay(reservation.getDaysOfStay()+1);
        reservationRepository.save(reservation);
        return null;
    }

    @Override
    public Page<Reservation> findPage(String guestName, RoomType roomType, Long hotel, int pageNum, int pageSize){
        Specification<Reservation> specification = Specification
                .where(filterContainsText(Reservation.class, "guestName", guestName))
                .and(filterEqualsV(Reservation.class, "roomType", roomType))
                .and(filterEquals(Reservation.class, "hotel.id", hotel));

        return this.reservationRepository.findAll(
                specification,
                PageRequest.of(pageNum, pageSize)
        );
    }


}
