package dashbikash.spring.springlearn;

public class Product {
	@Override
	public String toString() {
		return "Product [id=" + id + ", title=" + title + ", price=" + price + ", description=" + description
				+ ", category=" + category + ", image=" + image + ", RatingObject=" + RatingObject + "]";
	}

	private float id;
	private String title;
	private float price;
	private String description;
	private String category;
	private String image;
	Rating RatingObject;

	// Getter Methods

	public float getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public float getPrice() {
		return price;
	}

	public String getDescription() {
		return description;
	}

	public String getCategory() {
		return category;
	}

	public String getImage() {
		return image;
	}

	public Rating getRating() {
		return RatingObject;
	}

	// Setter Methods

	public void setId(float id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setRating(Rating ratingObject) {
		this.RatingObject = ratingObject;
	}
}
