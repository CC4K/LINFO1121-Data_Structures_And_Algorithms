package Exam_training;

import org.javagrader.ConditionalOrderingExtension;
import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.javagrader.TestResultStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@Grade
@ExtendWith(ConditionalOrderingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LFUCacheTest {


    @Test
    @Grade(value = 1)
    @Order(1)
    @GradeFeedback(message = "Debug it locally", on = TestResultStatus.FAIL)
    public void testSimple1() {
        LFUCache lfu = new LFUCache(2);
        lfu.put(1, 1);   // cache=[1,_], cnt(1)=1
        System.out.println(lfu.cache.keySet()+" "+lfu.frequency+" "+lfu.min_freq);

        lfu.put(2, 2);   // cache=[2,1], cnt(2)=1, cnt(1)=1
        System.out.println(lfu.cache.keySet()+" "+lfu.frequency+" "+lfu.min_freq);

        int res1 = lfu.get(1);      // cache=[1,2], cnt(2)=1, cnt(1)=2 // return 1
        System.out.println(lfu.cache.keySet()+" "+lfu.frequency+" "+lfu.min_freq+" get(): "+res1);
        assertEquals(1, res1);

        lfu.put(3, 3);   // 2 is the LFU key because cnt(2)=1 is the smallest, invalidate 2. // cache=[3,1], cnt(3)=1, cnt(1)=2
        System.out.println(lfu.cache.keySet()+" "+lfu.frequency+" "+lfu.min_freq);

        lfu.get(2);      // return -1 (not found)

        int res2 = lfu.get(3);      // cache=[3,1], cnt(3)=2, cnt(1)=2 // return 3
        System.out.println(lfu.cache.keySet()+" "+lfu.frequency+" "+lfu.min_freq+" get(): "+res2);
        assertEquals(3, res2);

        lfu.put(4, 4);   // Both 1 and 3 have the same cnt, but 1 is LRU, invalidate 1. // cache=[4,3], cnt(4)=1, cnt(3)=2
        System.out.println(lfu.cache.keySet()+" "+lfu.frequency+" "+lfu.min_freq);

        lfu.get(1);      // return -1 (not found)

        int res3 = lfu.get(3);      // cache=[3,4], cnt(4)=1, cnt(3)=3 // return 3
        System.out.println(lfu.cache.keySet()+" "+lfu.frequency+" "+lfu.min_freq+" get(): "+res3);
        assertEquals(3, res3);

        int res4 = lfu.get(4);      // cache=[4,3], cnt(4)=2, cnt(3)=3 // return 4
        System.out.println(lfu.cache.keySet()+" "+lfu.frequency+" "+lfu.min_freq+" get(): "+res4);
        assertEquals(4, res4);
    }

}
