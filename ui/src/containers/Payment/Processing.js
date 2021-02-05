import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Dimmer, Loader} from "semantic-ui-react";
import './Processing.css';
import * as actions from '../../store/actions/index';
import processing from './atm_s.jpg';

class Processing extends Component {

    state = {
        processing: true
    }

    componentDidMount() {
        let params = new URLSearchParams(this.props.location.search);
        console.log(params.get("payment_intent"));
        console.log(params.get("payment_intent_client_secret"));

        if (params.get("payment_intent") !== null
            && params.get("payment_intent_client_secret") !== null){
            this.props.confirmOrder(params.get("payment_intent"),  window.localStorage.getItem("orderId"), this.props.history);
        }
    }

    render() {
        return (
            <div>
                <Dimmer active={this.state.processing} page>
                    <Loader content='Loading'/>
                </Dimmer>
                <img alt="" src={processing} className="Processing-Image-div"/>
            </div>
        )
    }

}

const mapPropsToState = (state) => {
    return {
        loading: state.order.loading
    }
}

const dispatchToProps = dispatch => {
    return {
        confirmOrder: (paymentIntentId, orderId, history) => dispatch(actions.confirmOrder(paymentIntentId, orderId, history))
    }
}

export default connect(mapPropsToState, dispatchToProps)(Processing);
