package mk.ukim.finki.wp.kol2024g1.service.Impl;

import mk.ukim.finki.wp.kol2024g1.model.Hotel;
import mk.ukim.finki.wp.kol2024g1.repository.HotelRepository;
import mk.ukim.finki.wp.kol2024g1.service.HotelService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;

    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public Hotel findById(Long id){
        return hotelRepository.findById(id).orElse(null);
    }

    @Override
    public List<Hotel> listAll(){
        return hotelRepository.findAll();
    }

    @Override
    public Hotel create(String name){
        return hotelRepository.save(new Hotel(name));
    }
}
