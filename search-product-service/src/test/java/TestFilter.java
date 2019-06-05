import com.project.model.Product;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.Format;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TestFilter {

    List<Product> productList = new ArrayList<>();


    @Before
    public void setup(){
        for (int i = 0; i <= 10; i++){
            productList.add(createProduct());
            productList.add(createProduct2());
            productList.add(createProduct3());

        }
    }


    @Test
    public void test(){

        Set<Product> treeSet = new TreeSet<Product>((o1, o2) -> {
                return o1.getId().compareTo(o2.getId());
        });

        Set<Product> treeSet2 = new TreeSet<Product>(Comparator.comparing(Product::getId).thenComparing(Product::getCategory));

        treeSet.addAll(productList);
        treeSet2.addAll(productList);

        System.out.println(treeSet2.size());

        Collection<Product> p = productList.stream().collect(
                Collectors.toMap(
                        obj -> obj.getId()+obj.getCategory(),
                        Function.identity(),
                        (o1,o2) -> o1))
                .values();


        Map<String, Product> mapP = productList.stream().collect(
                Collectors.toMap(pr -> pr.getId()+pr.getName(),
                        Function.identity(),
                        (pr1, pr2) -> pr1));



        assertEquals(3, mapP.values().size());
    }

    @Test
    public void testRemoveDuplicateWithClassicAlgo_UsingBothIdAndCategoryToFormOneID(){
        productList.sort((o1, o2) -> o1.getId().compareTo(o2.getId()));
        List<Product> listWithoutDuplicate = new ArrayList<>();

        int temp = 0;
        for (int i = 0; i< productList.size() -1; i++){
            String product1Id = productList.get(i).getId()+productList.get(i).getCategory();
            String product2Id = productList.get(i + 1).getId()+productList.get(i+1).getCategory();
            if (!product1Id.equals(product2Id)){
                listWithoutDuplicate.add(temp, productList.get(i));
                temp++;
            }
        }
        listWithoutDuplicate.add(temp, productList.get(productList.size() - 1));

        assertEquals(3, listWithoutDuplicate.size());
    }

    @Test
    public void testRemoveDuplicateWithClassicAlgo_UsingOrTruthTable(){
        productList.sort(Comparator.comparing(Product::getId).thenComparing(Product::getCategory));
        List<Product> listWithoutDuplicateOr = new ArrayList<>();
        int tempOr = 0;
        for (int i = 0; i< productList.size() -1; i++){

            if (!productList.get(i).getId().equals(productList.get(i+1).getId())
                    || !productList.get(i).getName().equals(productList.get(i+1).getName())){
                listWithoutDuplicateOr.add(tempOr, productList.get(i));
                tempOr++;
            }
        }
        listWithoutDuplicateOr.add(tempOr, productList.get(productList.size() - 1));

        assertEquals(3, listWithoutDuplicateOr.size());
    }

    @Test
    public void testRemoveDuplicateWithClassicAlgo_UsingOrTruthTable_AndTheSameList(){
        productList.sort(Comparator.comparing(Product::getId).thenComparing(Product::getCategory));
        int tempOr = 0;
        for (int i = 0; i< productList.size() - 1; i++){

            if (productList.get(i).getId().equals(productList.get(i+1).getId())
                    && productList.get(i).getCategory().equals(productList.get(i+1).getCategory())){
                productList.remove(i);
                --i;
            }
        }

        System.out.println(productList);
        assertEquals(3, productList.size());
    }

    @Test
    public void testRemoveDuplicateWithClassicAlgo_UsingOrTruthTable_OtherList(){
        productList.sort(Comparator.comparing(Product::getId).thenComparing(Product::getCategory));

        List<Product> newList = new ArrayList<>();
        int tempOr = 0;
        for (int i = 0; i < productList.size() - 1; i++){

            if (!productList.get(i).getId().equals(productList.get(i+1).getId())){
                newList.add(tempOr, productList.get(i));
                tempOr++;
            }
        }
        newList.add(tempOr, productList.get(productList.size() - 1));
        System.out.println(newList);
        assertEquals(3, newList.size());
    }


    @Test
    public void testRemoveDuplicateWithClassicAlgo_UsingOrTruthTable_UsingSet(){
        productList.sort(Comparator.comparing(Product::getId).thenComparing(Product::getCategory));

        Set<Product> productSet = new HashSet<>(productList);

        productSet.iterator().next();
        assertEquals(3, productSet.size());
    }

    @Test
    public void testDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateParsedAsString = now.format(formatter);

        System.out.println(LocalDateTime.parse(dateParsedAsString, formatter));
        System.out.println(LocalDateTime.now());

    }

    private Product createProduct() {
        Product p = new Product();
        p.setId("111111");
        p.setName("MK");
        p.setCategory("US");
        return p;
    }

    private Product createProduct2() {
        Product p = new Product();
        p.setId("222222");
        p.setName("MK");
        p.setCategory("NS");
        return p;
    }

    private Product createProduct3() {
        Product p = new Product();
        p.setId("33333");
        p.setName("EC");
        p.setCategory("NS");
        return p;
    }
}
