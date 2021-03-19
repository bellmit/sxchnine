import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Image, Label, List, Table} from "semantic-ui-react";
import Aux from '../../../../adhoc/Aux/Aux';
import './GirdOrders.css';
import * as actions from "../../../../store/actions";


class GridOrders extends Component {

    handleOrder = (orderId, history) => this.props.getOrderById(orderId, this.props.history);

    getColor(orderStatus) {
        if (orderStatus === 'WAITING'){
            return 'hsl(228, 100%, 85%)';
        } else if (orderStatus === 'REQUIRED_ACTION'){
            return 'hsl(310, 88%, 77%)';
        } else if (orderStatus === 'CONFIRMED'){
            return 'hsl(209, 88%, 54%)';
        } else if (orderStatus === 'PROCESSING'){
            return 'hsl(44, 100%, 50%)';
        } else if (orderStatus === 'REFUSED'){
            return 'hsl(358, 100%, 63%)';
        } else if (orderStatus === 'SHIPPED'){
            return 'green';
        }
    }

    render() {

        let searchErrors = undefined;
        if (this.props.searchOrdersError !== undefined){
            searchErrors = <Label color='red'>Search Failed:{this.props.searchOrdersError.message}</Label>
        }

        let selectOrderErrors = undefined;
        if (this.props.orderByIdError !== undefined){
            selectOrderErrors = <Label color='red'>Cannot select Order:{this.props.orderByIdError.message}</Label>
        }

        let header = <Table.Header>
            <Table.Row>
                <Table.HeaderCell>Order ID</Table.HeaderCell>
                <Table.HeaderCell>Email</Table.HeaderCell>
                <Table.HeaderCell>Products</Table.HeaderCell>
                <Table.HeaderCell>Total</Table.HeaderCell>
                <Table.HeaderCell>Address</Table.HeaderCell>
                <Table.HeaderCell>Order Time</Table.HeaderCell>
                <Table.HeaderCell singleLine>Order Status</Table.HeaderCell>
                <Table.HeaderCell singleLine>Payment Status</Table.HeaderCell>
                <Table.HeaderCell singleLine>Shipping Status</Table.HeaderCell>
                <Table.HeaderCell singleLine>Shipping Time</Table.HeaderCell>
                <Table.HeaderCell singleLine>TrackingNumber</Table.HeaderCell>
            </Table.Row>

        </Table.Header>

        let ordersBody = undefined;

        if (this.props.searchOrdersData) {
            ordersBody = <Aux>
                <Table color='violet' size='small' collapsing compact>
                    {header}
                    {this.props.searchOrdersData.map((wo, index) => (
                        <Table.Body key={index}>
                            <Table.Row>
                                <Table.Cell selectable>
                                        <span className="search-orders-id-text"
                                              onClick={() => this.handleOrder(wo.orderId)}>{wo.orderId}</span>
                                </Table.Cell>
                                <Table.Cell>{wo.userEmail}</Table.Cell>
                                {wo.products.map((p, idxP) => (
                                    <List key={idxP} size='big'>
                                        <List.Item>
                                            <List.Content>
                                                <List.Header>
                                                    <Image avatar src={p.image} size='mini'/>
                                                    <br/>
                                                    {p.productBrand} {p.productName}
                                                </List.Header>
                                                <List.Description>
                                                    {p.productSize} {p.productColor}
                                                </List.Description>
                                            </List.Content>
                                        </List.Item>
                                    </List>
                                ))}
                                <Table.Cell>{wo.total}</Table.Cell>
                                <Table.Cell singleLine>{wo.userAddress.address} {wo.userAddress.city}</Table.Cell>
                                <Table.Cell singleLine>{wo.orderTime}</Table.Cell>
                                <Table.Cell style={{backgroundColor: this.getColor(wo.orderStatus)}}>
                                    <span style={{fontWeight: 'bold'}}>{wo.orderStatus}</span>
                                </Table.Cell>
                                <Table.Cell>
                                    <span style={{fontWeight: 'bold'}}>{wo.paymentStatus}</span>
                                </Table.Cell>
                                <Table.Cell singleLine>{wo.shippingStatus}</Table.Cell>
                                <Table.Cell singleLine>{wo.shippingTime}</Table.Cell>
                                <Table.Cell singleLine>{wo.trackingNumber}</Table.Cell>
                            </Table.Row>
                        </Table.Body>
                    ))}

                </Table>
            </Aux>
        }

        return (
            <div className="table-search-orders">
                {searchErrors}
                {selectOrderErrors}
                {ordersBody}
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        searchOrdersData: state.orders.searchOrdersData,
        orderByIdError: state.orders.orderByIdError,
        searchOrdersError: state.orders.searchOrdersError
    }
}

const dispatchToProps = dispatch => {
    return {
        getOrderById: (orderId, history) => dispatch(actions.orderById(orderId, history)),
    }
}

export default connect(mapStateToProps, dispatchToProps)(GridOrders);