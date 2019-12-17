package com.github.modules.monitor.repository;

import com.github.modules.monitor.domain.Visits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Repository
public interface VisitsRepository extends JpaRepository<Visits,Long> {

    /**
     * findByDate
     * @param date 日期
     * @return Visits
     */
    Visits findByDate(String date);

    /**
     * 获得一个时间段的记录
     * @param date1 日期1
     * @param date2 日期2
     * @return List
     */
    @Query(value = "select * FROM visits where create_time between ?1 and ?2",nativeQuery = true)
    List<Visits> findAllVisits(String date1, String date2);
}
