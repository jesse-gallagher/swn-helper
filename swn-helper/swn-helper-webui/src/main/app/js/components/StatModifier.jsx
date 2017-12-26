import React, {Component} from "react";

export default class StatModifier extends Component {
    render() {
		const { value } = this.props;
		
		/* TODO figure out why StatModifiers break after first change */
		/*if(typeof value !== "number") {
			return <span className="stat-modifier">-</span>
		} else if(value >= 18) {
			return <span className="stat-modifier">+2</span>
		} else if(value >= 14) {
			return <span className="stat-modifier">+1</span>
		} else if(value >= 8) {
			return <span className="stat-modifier">0</span>
		} else if(value >= 4) {
			return <span className="stat-modifier">-1</span>
		} else {
			return <span className="stat-modifier">-2</span>
		}*/
		return <span/>
    }
}
