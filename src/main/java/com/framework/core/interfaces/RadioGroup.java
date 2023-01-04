package com.framework.core.interfaces;

import java.util.List;
import com.framework.core.interfaces.impl.RadioGroupImpl;
import com.framework.core.interfaces.impl.internal.ImplementedBy;

/**
 * Interface for a select element.
 */
@ImplementedBy(RadioGroupImpl.class)
public interface RadioGroup extends Element {

	/**
	 * @summary - Allows a radio button to be selected by its index within the group
	 */
	public void selectByIndex(int index);

	/**
	 * @summary - Returns a List<String> of all options in the radio group
	 * @return List<String> - all options in the radio group
	 */
	public List<String> getAllOptions();

	/**
	 * @summary - Allows a radio button to be selected by its value/option text
	 */
	public void selectByOption(String option);

	/**
	 * @summary - Returns the number of values/options found in the radio group
	 * @return int - number of values/options found in the radio group
	 */
	public int getNumberOfOptions();

	/**
	 * @summary - Returns the value/option of the selected radio button
	 * @return String - value/option of the selected radio button
	 */
	public String getSelectedOption();

	/**
	 * @summary - Returns the index of the selected radio button
	 * @return int - index of the selected radio button
	 */
	public int getSelectedIndex();

}