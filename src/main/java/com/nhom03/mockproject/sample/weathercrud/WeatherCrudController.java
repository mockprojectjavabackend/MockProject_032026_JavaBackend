package com.nhom03.mockproject.sample.weathercrud;

import com.nhom03.mockproject.sample.weathercrud.model.Weather;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherCrudController {

    private List<Weather> weatherData = new ArrayList<>(List.of(
            new Weather(1, "Da Nang", 25.0),
            new Weather(2, "Ha Noi", 20.5)
    ));

    @GetMapping
    public List<Weather> getAll() {
        return weatherData;
    }

    @PostMapping
    public String create(@RequestBody Weather newWeather) {
        weatherData.add(newWeather);
        return "Thêm thành công thành phố: " + newWeather.getCity();
    }

    @PutMapping("/{id}")
    public String update(@PathVariable int id, @RequestBody Weather updateWeather) {
        for (Weather w : weatherData) {
            if (w.getId() == id) {
                w.setCity(updateWeather.getCity());
                w.setTemp(updateWeather.getTemp());
                return "Cập nhật ID " + id + " thành công!";
            }
        }
        return "Không tìm thấy ID để cập nhật.";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        boolean removed = weatherData.removeIf(w -> w.getId() == id);
        return removed ? "Xóa thành công ID: " + id : "Không tìm thấy ID để xóa.";
    }
}