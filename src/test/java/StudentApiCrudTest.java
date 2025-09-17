import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentApiCrudTest {

    private static final String BASE_URL = "http://localhost:8080"; // kendi API adresimi girdim. Örnek bir API adresi olmadığı için.

    @Test
    @Order(1)
    void testCreateStudent() {
        Student student = new Student("123", "Ali", "Veli", 16, "10A", "male", "T001");
        //student diye bir obje oluşturdum, bunun içerisine sizden ek olarak soyisim, sınıf ve rehber öğretmenininId sini ekledim.
        //Aslında Öğrenci numaraları backendden gelmesi gerekiyor ama burda ben manuel olarak giriyorum. (yani boş göndermem gerekiyor, backend otomatik sırayla atayacak.)
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(student)
                .post(BASE_URL + "/students");

        assertEquals(201, response.statusCode());
        assertTrue(response.getBody().asString().contains("Ali"));
    }

    @Test
    @Order(2)
    void testGetStudent() {
        Response response = RestAssured.get(BASE_URL + "/students/123"); //burada id si "123" olan öğrenciyi çekiyorum ve bana dönen json içerisinde "Ali" değeri var mı kontrol ediyorum.

        assertEquals(200, response.statusCode());
        assertTrue(response.getBody().asString().contains("Ali"));
    }

    @Test
    @Order(3)
    void testUpdateStudent() { //burada id si 123 olan öğrenciye istek atıyorum.
                               // örnek olarak başta yaşını 17, sınıfını 11B ve rehber öğretmenini değiştiriyorum.
        Student updated = new Student("123", "Ali", "Veli", 17, "11B", "male", "T002");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updated)
                .put(BASE_URL + "/students/123");

        assertEquals(200, response.statusCode());
        assertTrue(response.getBody().asString().contains("11B"));
    }

    @Test
    @Order(4) //orderlara sayı vermemin sebebi sırayla çalışması. 1 İLK olarak çalışacak, sırayla 4 de bitecek.
    void testDeleteStudent() { //burada direkt öğrenciId si ile silme işlemi gerçekleştiriyorum
                                //doğrulamasını ise dönen response' un 200 olduğunu doğrulayarak test ediyorum.
        Response response = RestAssured.delete(BASE_URL + "/students/123");

        assertEquals(200, response.statusCode());
    }

    // Öğrenci classını json olarak göndermek için bu objeyi oluşturuyorum
    // bu yazdığım objenin değerleri, yukarıda test yazarken bana yardımcı oluyor.
    static class Student {
        public String studentId;
        public String firstName;
        public String lastName;
        public int age;
        public String grade;
        public String gender;
        public String teacherId;

        public Student(String studentId, String firstName, String lastName,
                       int age, String grade, String gender, String teacherId) {
            this.studentId = studentId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.grade = grade;
            this.gender = gender;
            this.teacherId = teacherId;
        }
    }
}
