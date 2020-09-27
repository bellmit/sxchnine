import React, {Component} from 'react';
import {connect} from 'react-redux';
import Aux from '../../hoc/Aux/Aux';
import Banner from "../../components/Banner/Banner";
import './UserAccount.css';
import Contact from "../Contact/Contact";
import TabAccount from "./TabAccount";
import salute1 from "./salute1.png";


class UserAccount extends Component {

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
                    Hi {this.props.user.firstName} {this.props.user.lastName}, Welcome back ! <img src={salute1} alt="salute" style={{height: '5%', width: '5%'}} />
                </span>

                <div className="UserTabAccount-Container">
                    <TabAccount />

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

export default connect(mapPropsToState, null)(UserAccount);