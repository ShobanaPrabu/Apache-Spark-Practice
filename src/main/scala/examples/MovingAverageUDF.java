package examples;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 *
 *
 * https://simplyanalyticsblog.wordpress.com/2014/06/05/how-to-implement-a-moving-average-in-apache-hive/
 * https://stackoverflow.com/questions/25082057/hive-sum-over-a-specified-group-hiveql
 *
 *
 * Created by vdokku on 6/28/2017.
 */

@Description(name = "moving_avg", value = "_FUNC_(n, key, value) - Return the average of the last n values for the specified key", extended = "Example:n"
        + " > SELECT _FUNC_(50, symbol, price_close) FROM stocks LIMIT 100;n"
        + " 25.73n")
public class MovingAverageUDF extends UDF {
    private Map<Text, LinkedList<Float>> map = new HashMap<>();
    /**
     * In the HIVE Function, we will pass in NumUnits, key, Value.
     * <p>
     * Now I need moving average.
     *
     * @param numberOfUnits
     * @param key
     * @param value
     * @return
     */
    public float evaluate(final int numberOfUnits, final Text key, final float value) {
        LinkedList<Float> list = map.get(key);

        if (list == null) {
            list = new LinkedList<>();
            map.put(key, list);
        }

        list.add(value);
        if (list.size() > numberOfUnits) {
            list.removeFirst(); // Why we need to remove ??
        }

        if (numberOfUnits == 0) {
            return 0.0f;
        } else {
            int size = list.size();
            int n = size < numberOfUnits ? size : numberOfUnits;
            return sum(list) / (1.0f * n);
        }
    }

    private float sum(LinkedList<Float> list) {
        float result = 0.0F;
        for (float f : list) {
            result += f;
        }
        return result;
    }

    /*

select
t2.date,
round(count(t1.users)) as users_inmarket
from (select distinct to_date(datehour) date
      from store.exposure
      where datehour >= '2014-05-01 00:00:00' ) t2
inner join (select to_date(datehour) date, count(distinct userpid) users
             from store.exposure
             where datehour between '2014-05-01 00:00:00' and '2014-05-31 23:59:59'
             group by to_date(datehour) ) t1
where t2.date is not null
  and datediff(t2.date, t1.date) between 0 and 21
group by t2.date

     */

}
