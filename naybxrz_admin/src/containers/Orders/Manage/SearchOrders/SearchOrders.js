import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Dimmer, Dropdown, Form, Loader, Segment} from "semantic-ui-react";
import * as actions from '../../../../store/actions';
import loop from './loop.png';
import '../ManageOrders.css';

class SearchOrders extends Component {

    state = {
        orderId: '',
        email: '',
        orderStatus: ''
    }

    handleChange = (e, {name, value}) => this.setState({[name]: value});

    handleChangeOrderStatus = (e, {value}) => this.setState({orderStatus: value});

    handleSearch = () => {
        console.log(this.state.orderStatus)
        if (this.state.orderStatus !== '') {
            this.props.searchOrdersByStatus(this.state.orderStatus);
        } else {
            this.props.searchOrdersByIdAndEmail(this.state.orderId, this.state.email);
        }
    }

    render() {

        const orders = [
            {key: 0, text: '----', value: ''},
            {key: 1, text: 'WAITING', value: 'WAITING'},
            {key: 2, text: 'REQUIRED_ACTION', value: 'REQUIRED_ACTION'},
            {key: 3, text: 'CONFIRMED', value: 'CONFIRMED'},
            {key: 4, text: 'PROCESSING', value: 'PROCESSING'},
            {key: 5, text: 'SHIPPED', value: 'SHIPPED'},
            {key: 6, text: 'REFUSED', value: 'REFUSED'}
        ]

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

                            <Dropdown style={{fontSize: 'smaller'}}
                                onChange={this.handleChangeOrderStatus}
                                placeholder='Order Status'
                                options={orders}
                                selection
                                value={this.state.orderStatus}/>

                            <img alt="search" src={loop} className="search-loop"
                                 onClick={this.handleSearch}/>
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
        searchOrdersByIdAndEmail: (orderId, email) => dispatch(actions.searchOrders(orderId, email)),
        searchOrdersByStatus: (orderStatus) => dispatch(actions.searchOrdersByStatus(orderStatus))
    }
}

export default connect(mapStateToProps, dispatchToProps)(SearchOrders);