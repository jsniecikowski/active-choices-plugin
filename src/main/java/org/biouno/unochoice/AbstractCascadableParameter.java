package org.biouno.unochoice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.bind.JavaScriptMethod;

/**
 * Base class for cascadable parameters, providing basic and utility methods.
 *
 * @author Bruno P. Kinoshita
 * @since 0.20
 */
public abstract class AbstractCascadableParameter extends AbstractScriptableParameter implements CascadableParameter<Map<Object, Object>> {

	/*
	 * Serial UID. 
	 */
	private static final long serialVersionUID = 6992538803651219246L;
	/**
	 * Map with parameters in the UI.
	 */
	protected final Map<Object, Object> parameters = new LinkedHashMap<Object, Object>();
	
	/**
	 * Referenced parameters.
	 */
	private final String referencedParameters;
	
	protected AbstractCascadableParameter(String name, String description,
			String script, String fallbackScript, String referencedParameters) {
		super(name, description, script, fallbackScript);
		this.referencedParameters = referencedParameters;
	}

	/*
	 * (non-Javadoc)
	 * @see org.biouno.unochoice.CascadableParameter#getReferencedParameters()
	 */
	public String getReferencedParameters() {
		return referencedParameters;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.biouno.unochoice.AbstractScriptableParameter#getParameters()
	 */
	public Map<Object, Object> getParameters() {
		return parameters;
	}
	
	// --- methods called from the UI
	
	/*
	 * (non-Javadoc)
	 * @see org.biouno.unochoice.CascadableParameter#doUpdate(java.lang.String)
	 */
	@JavaScriptMethod
	public void doUpdate(String parameters) {
		getParameters().clear();
		final String[] params = parameters.split(SEPARATOR);
		for (String param : params) {
			final String[] nameValue = param.split("=");
			if (nameValue.length == 2) {
				final String name = nameValue[0];
				final String value = nameValue[1];
				getParameters().put(name, value);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.biouno.unochoice.CascadableParameter#getChoicesForUI()
	 */
	@JavaScriptMethod
	public List<Object> getChoicesForUI() {
		Map<Object, Object> mapResult = getChoices(getParameters());
		return Arrays.<Object>asList(mapResult.values(), mapResult.keySet());
	}
	
	public String[] getReferencedParametersAsArray() {
		String referencedParameters = this.getReferencedParameters();
		if (StringUtils.isNotBlank(referencedParameters)) {
			String[] array = referencedParameters.split(",");
			List<String> list = new ArrayList<String>();
			for (String value : array) {
				value = value.trim();
				if (StringUtils.isNotBlank(value)) {
					list.add(value);
				}
			}
			return list.toArray(new String[0]);
		}
		return new String[]{};
	}
	
}
