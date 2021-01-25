import React, {Component} from 'react';
import {connect} from 'react-redux';
import Aux from '../../hoc/Aux/Aux';
import Banner from "../../components/Banner/Banner";
import './UserAccount.css';
import Contact from "../Contact/Contact";
import TabAccount from "./TabAccount";
import salute1 from "./salute1.png";
import {Icon} from "semantic-ui-react";
import * as actions from "../../store/actions";


class UserAccount extends Component {

    signOff = () => {
        this.props.signOffUser(this.props.history);
    }

    render() {
        return (
            <Aux>
                <div className="UserAccount-Yellow-bar-div"/>
                <div>
                    <header>
                        <Banner {...this.props}/>
                    </header>
                </div>


                <span className="TabAccount-Welcome-Message">
                    Hi {this.props.user.firstName} {this.props.user.lastName}, Welcome back !
                    <img src={salute1} alt="salute" className="TabAccount-Welcome-Icon" />
                    <Icon name='power off' color='red' className="log-off-icon" onClick={this.signOff}>
                        <span className="log-off-text">Sign off</span>
                    </Icon>
                </span>

                <div className="UserTabAccount-Container">
                    <TabAccount {...this.props} />

                    <div className="UserTabAccount-Empty-Div"/>

                    <div className="UserTabAccount-footer">
                        <Contact/>
                    </div>
                </div>
            </Aux>
        );
    }
}

const mapPropsToState = state => {
    return {
        user: state.users.userAuthenticated
    }
}

const dispatchToProps = dispatch => {
    return {
        signOffUser: (history) => dispatch(actions.signOffUser(history))
    }
}

export default connect(mapPropsToState, dispatchToProps)(UserAccount);