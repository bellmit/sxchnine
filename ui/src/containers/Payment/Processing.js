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
                <img alt="naybxrz econcept store vintage clothes 90's adidas carhartt obey supreme nike nocta bombers wutang chimodu hip hop culture streetwear lifestyle hoodies shirts hat bob"
                     src={processing}
                     className="Processing-Image-div"/>
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
