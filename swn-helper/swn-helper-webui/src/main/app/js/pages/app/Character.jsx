import React, { Component } from "react";
import { withRouter } from 'react-router'
import { connect } from 'react-redux'
import { Field, reduxForm, FieldArray } from 'redux-form';
import { Link, Prompt } from "react-router-dom";
import { Button } from 'react-bootstrap';

import { Jsql } from '@darwino/darwino';
import { JsonDebug } from "@darwino/darwino-react";
import { DocumentForm, ComputedField,
         renderField, renderRadioGroup, renderCheckbox, renderSelect, renderRichText, renderDatePicker } from '@darwino/darwino-react-bootstrap';

import Constants from "./Constants";

import StatModifier from "../../components/StatModifier";

const DATABASE = Constants.DATABASE;
const STORE = "_default";

const FORM_NAME = "Character";

export class CharacterForm extends DocumentForm {

    // Default values of the properties
    static defaultProps  = {
        databaseId: DATABASE,
        storeId: STORE
    };

    constructor(props) {
        super(props)

        new Jsql()
            .database(props.databaseId)
            .query("SELECT $.name name, _unid FROM _default WHERE $.form='Player' ORDER BY name")
            .fetch()
            .then((json) => {
                this.setState({allPlayers: json.map((val) => { return {label: val.name, value: val.unid} })})
            })
        new Jsql()
            .database(props.databaseId)
            .query("SELECT $.name name, _unid FROM _default WHERE $.form='Planet' ORDER BY name")
            .fetch()
            .then((json) => {
                this.setState({allPlanets: json.map((val) => { return {label: val.name, value: val.unid} })})
            })
        
        this.renderSkillsTable = this.renderSkillsTable.bind(this);
    }

    createActionBar() {
        return (
            <div className="action-bar">
            </div>
        );
    }

    // Transform the generic attachment links to physical ones
    prepareForDisplay(values) {
    }

    // Transform the physical attachment links back to generic ones
    prepareForSave(values) {
    }

    // Default values when a new document is created
    defaultValues(values) {
        values.form = FORM_NAME;
        values.stats = {};
        values.saves = {};
        values.skills = [];
    }

    // Values computed once when the document is loaded
    calculateOnLoad(values) {
        if(!values.stats) { values.stats = {} }
        if(!values.saves) { values.saves = {} }
        if(!values.skills) { values.skills = [] }
    }

    // Values computed every time the document is changed
    calculateOnChange(values) {
    }

    // Validation
    validate(values) {
        const errors = {};
        
        if(!values.playerId) {
            errors.playerId = "Missing Player";
        }

        return errors;
    }

    handleRemoveSkill(index) {
        const skills = this.getComputedValues().skills;
        if(skills) {
            skills.splice(index, 1);
            this.forceUpdate();
        }
    }

    renderSkillsTable({ fields }) {
        const readOnly = this.isReadOnly();
        const disabled = this.isDisabled();
        return (
            <table className="skills-pane">
                <thead>
                    <tr>
                        <th>Skills</th>
                        <th>Level</th>
                        <th>
                            <Button bsStyle="link" onClick={() => fields.push({ name: "", value: 0 })}>+</Button>
                        </th>
                    </tr>
                </thead>
                <tbody>
                    {fields.map((value, index) =>
                        <tr key={index}>
                            <th scope="row"><Field name={`${value}.name`} type="text" component={renderField} disabled={disabled} readOnly={readOnly}/></th>
                            <td><Field name={`${value}.value`} type="number" component={renderField} disabled={disabled} readOnly={readOnly} parse={value => Number(value)}/></td>
                            <td><Button bsStyle="link" onClick={() => fields.remove(index)}>-</Button></td>
                        </tr>
                    )}
                </tbody>
            </table>
        )
    }

    render() {
        const { newDoc, doc } = this.state;
        const { handleSubmit, dirty, reset, invalid, submitting, type } = this.props;
        const readOnly = this.isReadOnly();
        const disabled = this.isDisabled();

        return (
            <div>
                <form onSubmit={handleSubmit(this.handleUpdateDocument)}>
                    {this.createActionBar()}
                    <Prompt
                        when={dirty||newDoc}
                        message={location => (
                            `The document is modified and not saved yet.\nDo you want to leave the current page without saving it?`
                        )}
                    />                    
                    <fieldset>
                        <legend>Character</legend>

                        <div className="row">
                            <div className="col-md-12 col-sm-12">
                                <Field name="playerId" type="text" component={renderSelect} label="Player" disabled={disabled} readOnly={readOnly}
                                    options={this.state.allPlayers} emptyOption={true}/>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-md-4 col-sm-12">
                                <Field name="name" type="text" component={renderField} label="Name" disabled={disabled} readOnly={readOnly}/>
                            </div>
                            <div className="col-md-2 col-sm-12">
                                <Field name="className" type="text" component={renderSelect} label="Class" disabled={disabled} readOnly={readOnly}
                                    options={[
                                        { value: "", label: "" },
                                        { value: "Expert", label: "Expert" },
                                        { value: "Psychic", label: "Psychic" },
                                        { value: "Warrior", label: "Warrior" }
                                    ]}/>
                            </div>
                            <div className="col-md-1 col-sm-12">
                                <Field name="level" type="number" component={renderField} label="Level" disabled={disabled} readOnly={readOnly}
                                    parse={value => Number(value)} />
                            </div>
                            <div className="col-md-2 col-sm-12">
                                <Field name="experience" type="number" component={renderField} label="XP" disabled={disabled} readOnly={readOnly}
                                    parse={value => Number(value)} />
                            </div>
                            <div className="col-md-3 col-sm-12">
                                <Field name="homeworldId" type="text" component={renderSelect} label="Homeworld" disabled={disabled} readOnly={readOnly}
                                    options={this.state.allPlanets} emptyOption={true}/>
                            </div>
                        </div>

                        <div className="row">

                            {/* Stats pane */}
                            <div className="col-md-4 col-sm-12">
                                <table className="stats-pane">
                                    <tbody>
                                        <tr>
                                            <th>&nbsp;</th>
                                            <th>Score</th>
                                            <th>Mod</th>
                                        </tr>
                                        <tr>
                                            <th scope="row">Strength</th>
                                            <td><Field name="stats[strength]" type="number" component={renderField} disabled={disabled} readOnly={readOnly} parse={value => Number(value)} /></td>
                                            <td><StatModifier value={this.getFieldValue("stats[strength]")}/></td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Intelligence</th>
                                            <td><Field name="stats[intelligence]" type="number" component={renderField} disabled={disabled} readOnly={readOnly} parse={value => Number(value)} /></td>
                                            <td><StatModifier value={this.getFieldValue("stats[intelligence]")}/></td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Dexterity</th>
                                            <td><Field name="stats[dexterity]" type="number" component={renderField} disabled={disabled} readOnly={readOnly} parse={value => Number(value)} /></td>
                                            <td><StatModifier value={this.getFieldValue("stats[dexterity]")}/></td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Wisdom</th>
                                            <td><Field name="stats[wisdom]" type="number" component={renderField} disabled={disabled} readOnly={readOnly} parse={value => Number(value)} /></td>
                                            <td><StatModifier value={this.getFieldValue("stats[wisdom]")}/></td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Constitution</th>
                                            <td><Field name="stats[constitution]" type="number" component={renderField} disabled={disabled} readOnly={readOnly} parse={value => Number(value)} /></td>
                                            <td><StatModifier value={this.getFieldValue("stats[constitution]")}/></td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Charisma</th>
                                            <td><Field name="stats[charisma]" type="number" component={renderField} disabled={disabled} readOnly={readOnly} parse={value => Number(value)} /></td>
                                            <td><StatModifier value={this.getFieldValue("stats[charisma]")}/></td>
                                        </tr>

                                        <tr>
                                            <td colSpan="3">&nbsp;</td>
                                        </tr>

                                        <tr>
                                            <th scope="row">Hit Points</th>
                                            <td><Field name="hitPoints" type="number" component={renderField} disabled={disabled} readOnly={readOnly} parse={value => Number(value)} /></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Psi Points</th>
                                            <td><Field name="psiPoints" type="number" component={renderField} disabled={disabled} readOnly={readOnly} parse={value => Number(value)} /></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <th scope="row">System Strain</th>
                                            <td><Field name="systemStrain" type="number" component={renderField} disabled={disabled} readOnly={readOnly} parse={value => Number(value)} /></td>
                                            <td></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>

                            {/* Saves */}
                            <div className="col-md-4 col-sm-12">
                                <table className="saves-pane">
                                    <tbody>
                                        <tr>
                                            <th scope="row">Attack Bonus</th>
                                            <td><Field name="attackBonus" type="number" component={renderField} disabled={disabled} readOnly={readOnly} parse={value => Number(value)} /></td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Physical Effect Save</th>
                                            <td><Field name="saves[physicalEffect]" type="number" component={renderField} disabled={disabled} readOnly={readOnly} parse={value => Number(value)} /></td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Mental Effect Save</th>
                                            <td><Field name="saves[mentalEffect]" type="number" component={renderField} disabled={disabled} readOnly={readOnly} parse={value => Number(value)} /></td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Evasion Save</th>
                                            <td><Field name="saves[evasion]" type="number" component={renderField} disabled={disabled} readOnly={readOnly} parse={value => Number(value)} /></td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Technology Save</th>
                                            <td><Field name="saves[technology]" type="number" component={renderField} disabled={disabled} readOnly={readOnly} parse={value => Number(value)} /></td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Luck Save</th>
                                            <td><Field name="saves[luck]" type="number" component={renderField} disabled={disabled} readOnly={readOnly} parse={value => Number(value)} /></td>
                                        </tr>
                                    </tbody>
                                </table>
                                
                                <div className="row">
                                    <div className="col-md-6 col-sm-12">
                                        <Field name="currentHitPoints" type="number" component={renderField} label="Current HP" disabled={disabled} readOnly={readOnly} parse={value => Number(value)} />
                                    </div>
                                    <div className="col-md-6 col-sm-12">
                                        <Field name="currentPsiPoints" type="number" component={renderField} label="Current PP" disabled={disabled} readOnly={readOnly} parse={value => Number(value)} />
                                    </div>
                                </div>
                            </div>

                            {/* Skills */}
                            <div className="col-md-4 col-sm-12">
                                <FieldArray name="skills" component={this.renderSkillsTable}/>
                                <table className="skills-pane">
                                    <tfoot>
                                        <tr>
                                            <th scope="row">Unspent Skill Points</th>
                                            <td colSpan="2"><Field name="unspentSkillPoints" type="number" component={renderField} disabled={disabled} readOnly={readOnly} parse={value => Number(value)} /></td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </div>
                        </div>

                        <div>
                            <span style={(disabled||readOnly) ? {display: 'none'} : {}}>
                                <div className="pull-right">
                                    <Button onClick={this.handleDeleteDocument} bsStyle="danger" style={newDoc ? {display: 'none'} : {}}>Delete</Button>
                                </div>
                                <Button bsStyle="primary" type="submit" disabled={invalid||submitting}>Submit</Button>
                            </span>
                            <Button bsStyle="link" onClick={this.handleCancel}>Cancel</Button>
                        </div>
                        
                        {/*Uncomment to display the current JSON content*/}
                        <JsonDebug form={this.props.form}/>
                    </fieldset>
                </form>
            </div>
        );
  }
}

const form = reduxForm({
    form: FORM_NAME,
    validate: DocumentForm.validateForm,
    onChange: DocumentForm.onChange
});

export default withRouter(
    connect(null,DocumentForm.mapDispatchToProps)
        (form(CharacterForm))
)
