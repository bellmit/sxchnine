import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Dimmer, Form, Loader, Segment} from "semantic-ui-react";
import * as actions from '../../../../store/actions';
import loop from './loop.png';
import '../ManageOrders.css';

class SearchOrders extends Component {

    state = {
        orderId: '',
        email: ''
    }

    handleChange = (e, {name, value}) => this.setState({[name]: value});

    searchOrdersByIdAndEmail = () => {
        this.props.searchOrdersByIdAndEmail(this.state.orderId, this.state.email);
    }

    render() {
        return (
            <div className="manage-div">
                <Dimmer active={this.props.loading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                <Segment inverted color='yellow' className="segment-div">
                    <Form size='small'>
                        <Form.Group widths='1'>
                            <Form.Input inverted
                                        size='mini'
                                        placeholder='Order ID'
                                        name='orderId'
                                        value={this.state.orderId}
                                        onChange={this.handleChange}/>
                            <Form.Input inverted
                                        size='mini'
                                        placeholder='Email'
                                        name='email'
                                        value={this.state.email}
                                        onChange={this.handleChange}/>

                            <img alt="search" src={loop} className="search-loop"
                                 onClick={this.searchOrdersByIdAndEmail}/>
                        </Form.Group>
                    </Form>
                </Segment>
            </div>

        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.orders.searchOrdersLoading
    }
};

const dispatchToProps = dispatch => {
    return {
        searchOrdersByIdAndEmail: (orderId, email) => dispatch(actions.searchOrders(orderId, email))
    }
}

export default connect(mapStateToProps, dispatchToProps)(SearchOrders);