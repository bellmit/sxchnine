import React, {Component} from 'react';
import {connect} from "react-redux";
import Aux from '../../adhoc/Aux/Aux';
import salute1 from "../Home/salute1.png";
import './Welcome.css';

class Welcome extends Component {

    render() {
        return (
            <Aux>
                <span className="Welcome-Message-Div">
                    <img src={salute1} alt="salute" className="img-props"/>
                    Hi ! {this.props.authenticatedUser.firstName}
                </span>
            </Aux>
        )
    }
}

const mapStateToProps = state => {
    return {
        authenticatedUser: state.user.authenticatedUser
    }
}

export default connect(mapStateToProps)(Welcome);