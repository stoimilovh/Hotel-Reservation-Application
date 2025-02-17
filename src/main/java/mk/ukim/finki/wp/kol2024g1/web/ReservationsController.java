package mk.ukim.finki.wp.kol2024g1.web;

import mk.ukim.finki.wp.kol2024g1.model.Hotel;
import mk.ukim.finki.wp.kol2024g1.model.Reservation;
import mk.ukim.finki.wp.kol2024g1.model.RoomType;
import mk.ukim.finki.wp.kol2024g1.service.HotelService;
import mk.ukim.finki.wp.kol2024g1.service.ReservationService;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping({"/","/reservations"})
public class ReservationsController {
    private final ReservationService reservationService;
    private final HotelService hotelService;

    public ReservationsController(ReservationService reservationService, HotelService hotelService) {
        this.reservationService = reservationService;
        this.hotelService = hotelService;
    }

    @GetMapping
    public String listAll(@RequestParam(required = false) String guestName,
                          @RequestParam(required = false) RoomType roomType,
                          @RequestParam(required = false) Long hotel,
                          @RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          Model model) {
        Page<Reservation> page = reservationService.findPage(
                guestName, roomType, hotel, pageNum-1, pageSize
        );

        List<Hotel> hotels = hotelService.listAll();
        List<RoomType> roomTypes = new ArrayList<>();
        roomTypes.add(RoomType.SINGLE);
        roomTypes.add(RoomType.DOUBLE);
        List<Reservation> reservations = reservationService.listAll();
        model.addAttribute("page", page);
        model.addAttribute("reservations", page.getContent());
        model.addAttribute("hotels", hotels);
        model.addAttribute("roomTypes", roomTypes);

        return "list";
    }

    @GetMapping("/add")
    public String showAdd(Model model) {
        List<Hotel> hotels = hotelService.listAll();
        List<RoomType> roomTypes = new ArrayList<>();
        roomTypes.add(RoomType.SINGLE);
        roomTypes.add(RoomType.DOUBLE);
        model.addAttribute("hotels", hotels);
        model.addAttribute("roomTypes", roomTypes);
        return "form";
    }

    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable Long id, Model model) {
        List<Hotel> hotels = hotelService.listAll();
        List<RoomType> roomTypes = new ArrayList<>();
        roomTypes.add(RoomType.SINGLE);
        roomTypes.add(RoomType.DOUBLE);
        model.addAttribute("hotels", hotels);
        model.addAttribute("roomTypes", roomTypes);
        Reservation reservation = reservationService.findById(id);
        model.addAttribute("reservation",reservation);
        return "form";
    }

    @PostMapping("/add")
    public String create(@RequestParam String guestName,
                         @RequestParam LocalDate dateCreated,
                         @RequestParam Integer daysOfStay,
                         @RequestParam RoomType roomType,
                         @RequestParam Long hotelId) {
        reservationService.create(guestName, dateCreated, daysOfStay, roomType, hotelId);
        return "redirect:/reservations";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String guestName,
                         @RequestParam LocalDate dateCreated,
                         @RequestParam Integer daysOfStay,
                         @RequestParam RoomType roomType,
                         @RequestParam Long hotelId) {
        this.reservationService.update(id, guestName, dateCreated, daysOfStay, roomType, hotelId);
        return "redirect:/reservations";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        reservationService.delete(id);
        return "redirect:/reservations";
    }


    @PostMapping("/extend/{id}")
    public String extend(@PathVariable Long id) {
        reservationService.extendStay(id);
        return "redirect:/reservations";
    }
}
