package com.budhlani.reactiveprogramming;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsQuery;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@DgsComponent
public class MyProjectDataFetcher {

    List<Cars> carsList = List.of(new Cars("BMW",1,"22/08/2023","28/08/2023", Cars.STATUS.Booked),
            new Cars("BMW",1,"22/08/2023","28/08/2026", Cars.STATUS.NotBooked));

    @DgsQuery
    public synchronized Mono<List<Cars>> cars(){

        return Mono.just(carsList.stream().filter(getCarsPredicate() ).collect(Collectors.toList()));

    }

    @NotNull
    private static Predicate<Cars> getCarsPredicate() {

        return car -> {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse(car.expiryDate,dtf);
            System.out.println(date);
            return car.getStatus().equals(Cars.STATUS.NotBooked) && date.isAfter(LocalDate.now());
        };
    }


}
