package org.fortiss.pmwt.wex.ui.io.generic.model;

/**
 * Model for streamlining custom CSV content.
 */

public class CustomLogEntry implements Comparable< CustomLogEntry >
{
	private Long	requestStartTime	= null;
	private Long	requestEndTime		= null;
	private String	stateName			= null;
	private String	requestURL			= null;
	private String	hostIP				= null;
	private String	localIPAddress		= null;
	private String	method				= null;
	private String	encoding			= null;
	private String	sessionID			= null;
	private String	parameterNames		= null;
	private String	parameterValues		= null;
	private Integer	localPort			= null;

	public Long getRequestStartTime()
	{
		return requestStartTime;
	}

	public void setRequestStartTime( Long requestStartTime )
	{
		this.requestStartTime = requestStartTime;
	}

	public Long getRequestEndTime()
	{
		return requestEndTime;
	}

	public void setRequestEndTime( Long requestEndTime )
	{
		this.requestEndTime = requestEndTime;
	}

	public String getStateName()
	{
		return stateName;
	}

	public void setStateName( String stateName )
	{
		this.stateName = stateName;
	}

	public String getRequestURL()
	{
		return requestURL;
	}

	public void setRequestURL( String requestURL )
	{
		this.requestURL = requestURL;
	}

	public String getHostIP()
	{
		return hostIP;
	}

	public void setHostIP( String hostIP )
	{
		this.hostIP = hostIP;
	}

	public String getLocalIPAddress()
	{
		return localIPAddress;
	}

	public void setLocalIPAddress( String localIPAddress )
	{
		this.localIPAddress = localIPAddress;
	}

	public String getMethod()
	{
		return method;
	}

	public void setMethod( String method )
	{
		this.method = method;
	}

	public String getEncoding()
	{
		return encoding;
	}

	public void setEncoding( String encoding )
	{
		this.encoding = encoding;
	}

	public String getSessionID()
	{
		return sessionID;
	}

	public void setSessionID( String sessionID )
	{
		this.sessionID = sessionID;
	}

	public String getParameterNames()
	{
		return parameterNames;
	}

	public void setParameterNames( String parameterNames )
	{
		this.parameterNames = parameterNames;
	}

	public String getParameterValues()
	{
		return parameterValues;
	}

	public void setParameterValues( String parameterValues )
	{
		this.parameterValues = parameterValues;
	}

	public Integer getLocalPort()
	{
		return localPort;
	}

	public void setLocalPort( Integer localPort )
	{
		this.localPort = localPort;
	}

	@Override
	public int compareTo( CustomLogEntry customLogEntry )
	{
		return this.requestStartTime.compareTo( customLogEntry.getRequestStartTime() );
	}
}
