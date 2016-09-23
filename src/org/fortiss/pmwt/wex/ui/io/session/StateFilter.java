package org.fortiss.pmwt.wex.ui.io.session;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.fortiss.pmwt.wex.ui.io.session.model.Session;
import org.fortiss.pmwt.wex.ui.io.session.model.State;

/**
 * Used to filter instances of {@link org.fortiss.pmwt.wex.ui.io.session.model.Session} by state name/ignore list.
 */

@XmlRootElement( name = "stateFilter" )
@XmlAccessorType( XmlAccessType.NONE )
public class StateFilter
{
	/**
	 * Map of format: <Old state name,<state instance>>
	 */

	@XmlElement( name = "stateMap" )
	private Map< String, StateFilter.CState >	stateMap							= null;

	/**
	 * Session must start with state name.
	 */

	@XmlAttribute( name = "mustStartWithUseCase" )
	private String								m_strSessionMustStartWithUseCase	= null;

	/**
	 * Session must end with state name.
	 */

	@XmlAttribute( name = "mustEndWithUseCase" )
	private String								m_strSessionMustEndWithUseCase		= null;

	/**
	 * Transition threshold time (in ms).
	 */

	@XmlAttribute( name = "transitionThresholdTime" )
	private Long								m_lTransitionThresholdTime			= null;

	/**
	 * Constructor.
	 */

	public StateFilter()
	{
		this.stateMap = new HashMap< String, StateFilter.CState >();
	}

	/**
	 * @return Map of format: <Old state name,<state instance>>
	 */

	public Map< String, StateFilter.CState > getStateMap()
	{
		return this.stateMap;
	}

	/**
	 * Returns the new/modified state name for the provided old state name.
	 * 
	 * @param strOldStateName
	 *            Old state name to look for.
	 * @return New/Modified state name for the provided old state name.
	 */

	public String getNewStateNameByOldStateName( String strOldStateName )
	{
		StateFilter.CState state = this.stateMap.get( strOldStateName );
		return state != null ? state.getNewStateName() : null;
	}

	/**
	 * Adds an entry of format: <old state name,old state name or null>.
	 * 
	 * @param strInitialStateName
	 *            Old state name to be used as a key.
	 * @param bUseNullValueAsMapping
	 *            When true, null will be used as the new/modified state name, otherwise the old state name will also be set as the new/modified state
	 *            name.
	 * @return true if adding the initial state was successful, false otherwise.
	 */

	public boolean addInitialStateName( String strInitialStateName, boolean bUseNullValueAsMapping )
	{
		if( !this.stateMap.containsKey( strInitialStateName ) )
		{
			StateFilter.CState state = new StateFilter.CState( strInitialStateName, bUseNullValueAsMapping ? null : removeUnsupportedCharacters( strInitialStateName ), false, false );
			this.stateMap.put( strInitialStateName, state );

			return true;
		}

		return false;
	}

	/**
	 * Sets the new/modified state name for a given old state name.
	 * 
	 * @param strOldStateName
	 *            Old state name to look for.
	 * @param strNewStateName
	 *            New/Modified state name to set.
	 * @return true if setting the new/modified state name was successful, false otherwise.
	 */

	public boolean setNewStateName( String strOldStateName, String strNewStateName )
	{
		StateFilter.CState state = this.stateMap.get( strOldStateName );
		if( state != null )
		{
			state.setNewStateName( strNewStateName );
			return true;
		}

		return false;
	}

	/**
	 * Marks a certain state for removal (=ignore state).
	 * 
	 * @param strOldStateName
	 *            Old state name to be removed (=ignored).
	 * @param bRemove
	 *            true will mark the provided state name for removal.
	 */

	public void markStateForRemoval( String strOldStateName, boolean bRemove )
	{
		StateFilter.CState state = this.stateMap.get( strOldStateName );
		if( state != null )
		{
			state.setRemoveState( bRemove );
		}
	}

	/**
	 * Marks a certain state as mandatory (=must be part of the session)
	 * 
	 * @param strOldStateName
	 *            Old state name to be marked as mandatory.
	 * @param bMandatory
	 *            true will mark the state name as mandatory.
	 */

	public void markStateForMandatory( String strOldStateName, boolean bMandatory )
	{
		StateFilter.CState state = this.stateMap.get( strOldStateName );
		if( state != null )
		{
			state.setMandatoryState( bMandatory );
		}
	}

	/**
	 * Sets the state name the session must start with.
	 * 
	 * @param strUseCase
	 *            State name.
	 */

	public void setSessionMustStartWithUseCase( String strUseCase )
	{
		this.m_strSessionMustStartWithUseCase = strUseCase;
	}

	/**
	 * @return State name the session must start with.
	 */

	public String getSessionMustStartWithUseCase()
	{
		return this.m_strSessionMustStartWithUseCase;
	}

	/**
	 * Sets the state name the session must end with.
	 * 
	 * @param strUseCase
	 *            State name.
	 */

	public void setSessionMustEndWithUseCase( String strUseCase )
	{
		this.m_strSessionMustEndWithUseCase = strUseCase;
	}

	/**
	 * Sets the transition threshold time.
	 * 
	 * @param lTransitionThresholdTime
	 *            Transition threshold time.
	 */

	public void setTransitionThresholdTime( Long lTransitionThresholdTime )
	{
		this.m_lTransitionThresholdTime = lTransitionThresholdTime;
	}

	/**
	 * @return Transition threshold time.
	 */

	public Long getTransitionThresholdTime()
	{
		return this.m_lTransitionThresholdTime;
	}

	/**
	 * @return State name the session must end with.
	 */

	public String getSessionMustEndWithUseCase()
	{
		return this.m_strSessionMustEndWithUseCase;
	}

	/**
	 * Replaces state names (old -> new) in a session.
	 * 
	 * @param session
	 *            Current session.
	 */

	public void replaceStateNamesInSession( Session session )
	{
		for( State state : session.getStateList() )
		{
			String strOldStateName = state.getName();
			String strNewStateName = getNewStateNameByOldStateName( strOldStateName );

			if( strNewStateName != null )
			{
				state.setName( strNewStateName );
			}
		}
	}

	/**
	 * Checks if a given state name contains illegal characters.
	 * 
	 * @param strStateName
	 *            State name to be checked.
	 * @return true if the state name contains only valid characters, false otherwise.
	 */

	public static boolean isValidStateName( String strStateName )
	{
		for( char c : strStateName.toCharArray() )
		{
			if( !isSupportedCharacter( c ) )
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Debug only.
	 */

	@Override
	public String toString()
	{
		StringBuilder sbOut = new StringBuilder();

		Iterator< Map.Entry< String, StateFilter.CState >> iterator = this.stateMap.entrySet().iterator();
		while( iterator.hasNext() )
		{
			Map.Entry< String, StateFilter.CState > mapEntry = iterator.next();
			sbOut.append( mapEntry.getKey() + " = " + mapEntry.getValue().toString() + "\n" );
		}

		return sbOut.toString();
	}

	/**
	 * Removes all illegal characters from a given state name.
	 * 
	 * @param strValue
	 *            State name to be processed.
	 * @return State name that only contains legal characters.
	 */

	private String removeUnsupportedCharacters( String strValue )
	{
		char[] cValueArray = new char[ strValue.length() ];
		Arrays.fill( cValueArray, '\0' );

		int nIndex = 0;
		for( char c : strValue.toCharArray() )
		{
			if( isSupportedCharacter( c ) )
			{
				cValueArray[ nIndex++ ] = c;
			}
		}

		return new String( cValueArray, 0, nIndex );
	}

	/**
	 * Checks if a certain character is legal. Legal characters are: [a-zA-Z0-9_]
	 * 
	 * @param c
	 *            Character to be checked.
	 * @return true if the character is legal, false otherwise.
	 */

	private static boolean isSupportedCharacter( char c )
	{
		// a = 97
		// z = 122
		// A = 65
		// Z = 90
		// _ = 95

		return ( c >= 97 && c <= 122 ) || ( c >= 65 && c <= 90 ) || ( c == 95 );
	}

	/**
	 * Class that represents a state.
	 */

	@XmlRootElement( name = "state" )
	@XmlAccessorType( XmlAccessType.NONE )
	public static final class CState
	{
		/**
		 * New/Modified state name.
		 */

		@XmlAttribute( name = "newStateName" )
		private String	newStateName	= null;

		/**
		 * Old state name.
		 */

		@XmlAttribute( name = "oldStateName" )
		private String	oldStateName	= null;

		/**
		 * State marked for removal?
		 */

		@XmlAttribute( name = "removeState" )
		private boolean	removeState		= false;

		/**
		 * Must be contained in session
		 */

		@XmlAttribute( name = "mandatoryState" )
		private boolean	mandatoryState	= false;

		/**
		 * Constructor.
		 * 
		 * @param newStateName
		 *            New/Modified state name.
		 * @param oldStateName
		 *            Old state name.
		 * @param removeState
		 *            State marked for removal?
		 */

		public CState( String newStateName, String oldStateName, boolean removeState, boolean mandatoryState )
		{
			this.newStateName = newStateName;
			this.oldStateName = oldStateName;
			this.removeState = removeState;
			this.mandatoryState = mandatoryState;
		}

		/**
		 * Constructor.
		 */

		@SuppressWarnings( "unused" )
		private CState()
		{
			// -- XML Serialization
		}

		/**
		 * @return New/Modified state name.
		 */

		public String getNewStateName()
		{
			return newStateName;
		}

		/**
		 * Sets the new/modified state name.
		 * 
		 * @param newStateName
		 *            New/Modified state name.
		 */

		public void setNewStateName( String newStateName )
		{
			this.newStateName = newStateName;
		}

		/**
		 * @return Determines if current state is marked for removal.
		 */

		public boolean isRemoveState()
		{
			return removeState;
		}

		/**
		 * Un/Mark state for removal.
		 * 
		 * @param removeState
		 *            true marks the current state for removal, false unmarks the current state for removal.
		 */

		public void setRemoveState( boolean removeState )
		{
			this.removeState = removeState;
		}

		/**
		 * @return Old state name.
		 */

		public String getOldStateName()
		{
			return oldStateName;
		}

		/**
		 * Marks the state as mandatory.
		 * 
		 * @param mandatoryState
		 *            true marks the state as mandatory.
		 */

		public void setMandatoryState( boolean mandatoryState )
		{
			this.mandatoryState = mandatoryState;
		}

		/**
		 * @return true if the state is marked as mandatory, false otherwise.
		 */

		public boolean isMandatoryState()
		{
			return this.mandatoryState;
		}

		/**
		 * Debug only.
		 */

		@Override
		public String toString()
		{
			return "[" + this.newStateName + " | " + this.oldStateName + " | " + this.removeState + " | " + this.mandatoryState + "]; ";
		}
	}
}