package intraday;

public class Options
{
	private Integer changeInOpenInterest;
	private Integer strikePrice;
	private OptionType optionType;

	public OptionType getOptionType()
	{
		return optionType;
	}

	public void setOptionType(OptionType optionType)
	{
		this.optionType = optionType;
	}

	public Integer getChangeInOpenInterest()
	{
		return changeInOpenInterest;
	}

	public void setChangeInOpenInterest(Integer changeInOpenInterest)
	{
		this.changeInOpenInterest = changeInOpenInterest;
	}

	public Integer getStrikePrice()
	{
		return strikePrice;
	}

	public void setStrikePrice(Integer strikePrice)
	{
		this.strikePrice = strikePrice;
	}

	@Override public String toString()
	{
		final StringBuilder sb = new StringBuilder("Options{");
		sb.append("changeInOpenInterest=").append(changeInOpenInterest);
		sb.append(", strikePrice=").append(strikePrice);
		sb.append(", optionType=").append(optionType);
		sb.append('}');
		return sb.toString();
	}

	enum OptionType{
		CALL,
		PUT
	}
}
