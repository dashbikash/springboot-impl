package dashbikash.spring.springlearn;

public class Rating {
	@Override
	public String toString() {
		return "Rating [rate=" + rate + ", count=" + count + "]";
	}

	private float rate;
	private float count;

	// Getter Methods

	public float getRate() {
		return rate;
	}

	public float getCount() {
		return count;
	}

	// Setter Methods

	public void setRate(float rate) {
		this.rate = rate;
	}

	public void setCount(float count) {
		this.count = count;
	}
}
