package library.management.dtos;

import lombok.Data;

@Data
public class CreateBook {

	String title;
	String author;
	Integer publishedYear;

}
