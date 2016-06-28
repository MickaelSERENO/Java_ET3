package com.et3.Engine;

public class Message
{
	private int m_day;
	private String m_text;

	public Message(int day, String text)
	{
		m_day  = day;
		m_text = text;
	}

	public String toString()
	{
		return Integer.toString(m_day) + " : " + m_text;
	}

	public int getDay()
	{
		return m_day;
	}
}
