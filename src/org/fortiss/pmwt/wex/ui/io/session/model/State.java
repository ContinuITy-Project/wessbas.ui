package org.fortiss.pmwt.wex.ui.io.session.model;

/**
 * Model that represents a single state in a session.
 */

public class State
{
	private String	m_strName			= null;
	private Long	m_lTimeIn			= null;
	private Long	m_lTimeOut			= null;
	private String	m_strURL			= null;
	private Integer	m_nPort				= null;
	private String	m_strHostIP			= null;
	private String	m_strProtocol		= null;
	private String	m_strAction			= null;
	private String	m_strQueryString	= null;
	private String	m_strEncoding		= null;

	public State( String strName, Long lTimeIn, Long lTimeOut, String strURL, Integer iPort, String strHostIP, String strProtocol, String strAction, String strQueryString, String strEncoding )
	{
		this.m_strName = strName;
		this.m_lTimeIn = lTimeIn;
		this.m_lTimeOut = lTimeOut;
		this.m_strURL = strURL;
		this.m_nPort = iPort;
		this.m_strHostIP = strHostIP;
		this.m_strProtocol = strProtocol;
		this.m_strAction = strAction;
		this.m_strQueryString = strQueryString;
		this.m_strEncoding = strEncoding;
	}

	public void setName( String strName )
	{
		this.m_strName = strName;
	}

	public String getName()
	{
		return this.m_strName;
	}

	public Long getTimeIn()
	{
		return this.m_lTimeIn;
	}

	public Long getTimeOut()
	{
		return this.m_lTimeOut;
	}

	public String getURL()
	{
		return this.m_strURL;
	}

	public Integer getPort()
	{
		return this.m_nPort;
	}

	public String getHostIP()
	{
		return this.m_strHostIP;
	}

	public String getProtocol()
	{
		return this.m_strProtocol;
	}

	public String getAction()
	{
		return this.m_strAction;
	}

	public String getQueryString()
	{
		return this.m_strQueryString;
	}

	public String getEncoding()
	{
		return this.m_strEncoding;
	}

	@Override
	public String toString()
	{
		StringBuilder sbOut = new StringBuilder();

		sbOut.append( "Name = " + this.m_strName + "\n" );
		sbOut.append( "TimeIn = " + this.m_lTimeIn + "\n" );
		sbOut.append( "TimeOut = " + this.m_lTimeOut + "\n" );
		sbOut.append( "URL = " + this.m_strURL + "\n" );
		sbOut.append( "Port = " + this.m_nPort + "\n" );
		sbOut.append( "Host IP = " + this.m_strHostIP + "\n" );
		sbOut.append( "Protocol = " + this.m_strProtocol + "\n" );
		sbOut.append( "Action = " + this.m_strAction + "\n" );
		sbOut.append( "QueryString = " + this.m_strQueryString + "\n" );
		sbOut.append( "Encoding = " + this.m_strEncoding + "\n" );

		return sbOut.toString();
	}
}