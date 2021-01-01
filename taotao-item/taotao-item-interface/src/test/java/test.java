import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class test {

    @Test
    public void StreamTest() {
        List<String> stringList = new ArrayList<>();
        stringList.add("aa");
        stringList.add("ccc");
        List<Integer> collect = stringList.stream().map(s -> s.length()).collect(Collectors.toList());
        System.out.println(stringList.get(0));
    }
}
