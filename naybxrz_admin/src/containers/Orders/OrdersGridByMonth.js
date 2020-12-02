import React, {PureComponent} from 'react';
import {connect} from 'react-redux';
import {Image, Label, List, Table} from "semantic-ui-react";
import * as actions from '../../store/actions/index';
import Aux from "../../adhoc/Aux/Aux";
import './OrdersGridByMonth.css';

class OrdersGridByMonth extends PureComponent {

    handleOrder = (orderId) => this.props.getOrderById(orderId, this.props.history);

    render() {

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
            </Table.Row>

        </Table.Header>

        let waitingOrders = undefined;

        if (this.props.ordersByMonth.filter(o => o.orderStatus === 'WAITING')
            .length > 0) {

            waitingOrders =
                <Aux>
                    <Table color='violet' size='small' collapsing compact>
                        <Label ribbon color='violet' size='tiny'>Waiting Orders</Label>
                        {header}
                        {this.props.ordersByMonth.filter(o => o.orderStatus === 'WAITING').map((wo, index) => (
                            <Table.Body key={index}>
                                <Table.Row>
                                    <Table.Cell selectable>
                                        <span className="orders-id-text"
                                              onClick={() => this.handleOrder(wo.orderKey.orderId)}>{wo.orderKey.orderId}</span>
                                    </Table.Cell>
                                    <Table.Cell>{wo.orderKey.userEmail}</Table.Cell>
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
                                    <Table.Cell singleLine>{wo.orderKey.orderTime}</Table.Cell>
                                    <Table.Cell style={{backgroundColor: 'hsl(228, 100%, 85%)'}}>
                                        <span style={{fontWeight: 'bold'}}>{wo.orderStatus}</span>
                                    </Table.Cell>
                                    <Table.Cell>
                                        <span style={{fontWeight: 'bold'}}>{wo.paymentStatus}</span>
                                    </Table.Cell>
                                </Table.Row>
                            </Table.Body>
                        ))}

                    </Table>
                </Aux>
        }

        let requireActionsOrders = undefined;

        if (this.props.ordersByMonth.filter(o => o.orderStatus === 'REQUIRED_ACTION')
            .length > 0) {

            requireActionsOrders =
                <Aux>
                    <Table color='pink' size='small' collapsing compact>
                        <Label ribbon color='pink' size='tiny'>Action Req Orders</Label>
                        {header}
                        {this.props.ordersByMonth.filter(o => o.orderStatus === 'REQUIRED_ACTION').map((wo, index) => (
                            <Table.Body key={index}>
                                <Table.Row>
                                    <Table.Cell selectable>
                                        <span className="orders-id-text"
                                              onClick={() => this.handleOrder(wo.orderKey.orderId)}>{wo.orderKey.orderId}</span>
                                    </Table.Cell>
                                    <Table.Cell>{wo.orderKey.userEmail}</Table.Cell>
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
                                    <Table.Cell singleLine>{wo.orderKey.orderTime}</Table.Cell>
                                    <Table.Cell style={{backgroundColor: 'hsl(310, 88%, 77%)'}}>
                                        <span style={{fontWeight: 'bold'}}>{wo.orderStatus}</span>
                                    </Table.Cell>
                                    <Table.Cell>
                                        <span style={{fontWeight: 'bold'}}>{wo.paymentStatus}</span>
                                    </Table.Cell>
                                </Table.Row>
                            </Table.Body>
                        ))}

                    </Table>
                </Aux>
        }

        let confirmedOrders = undefined;

        if (this.props.ordersByMonth.filter(o => o.orderStatus === 'CONFIRMED')
            .length > 0) {

            confirmedOrders =
                <Aux>
                    <Table color='blue' size='small' collapsing compact>
                        <Label ribbon color='blue' size='tiny'>Confirmed Orders</Label>
                        {header}
                        {this.props.ordersByMonth.filter(o => o.orderStatus === 'CONFIRMED').map((wo, index) => (
                            <Table.Body key={index}>
                                <Table.Row>
                                    <Table.Cell selectable>
                                        <span className="orders-id-text"
                                              onClick={() => this.handleOrder(wo.orderKey.orderId)}>{wo.orderKey.orderId}</span>
                                    </Table.Cell>
                                    <Table.Cell>{wo.orderKey.userEmail}</Table.Cell>
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
                                    <Table.Cell singleLine>{wo.orderKey.orderTime}</Table.Cell>
                                    <Table.Cell style={{backgroundColor: 'hsl(209, 88%, 54%)'}}>
                                        <span style={{fontWeight: 'bold'}}>{wo.orderStatus}</span>
                                    </Table.Cell>
                                    <Table.Cell>
                                        <span style={{fontWeight: 'bold'}}>{wo.paymentStatus}</span>
                                    </Table.Cell>
                                </Table.Row>
                            </Table.Body>
                        ))}

                    </Table>
                </Aux>
        }

        let processingOrders = undefined;

        if (this.props.ordersByMonth.filter(o => o.orderStatus === 'PROCESSING')
            .length > 0) {

            processingOrders =
                <Aux>
                    <Table color='orange' size='small' collapsing compact>
                        <Label ribbon color='orange' size='tiny'>Processing Orders</Label>
                        {header}
                        {this.props.ordersByMonth.filter(o => o.orderStatus === 'PROCESSING').map((wo, index) => (
                            <Table.Body key={index}>
                                <Table.Row>
                                    <Table.Cell selectable>
                                        <span className="orders-id-text"
                                              onClick={() => this.handleOrder(wo.orderKey.orderId)}>{wo.orderKey.orderId}</span>
                                    </Table.Cell>
                                    <Table.Cell>{wo.orderKey.userEmail}</Table.Cell>
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
                                    <Table.Cell singleLine>{wo.orderKey.orderTime}</Table.Cell>
                                    <Table.Cell style={{backgroundColor: 'hsl(44, 100%, 50%)'}}>
                                        <span style={{fontWeight: 'bold'}}>{wo.orderStatus}</span>
                                    </Table.Cell>
                                    <Table.Cell>
                                        <span style={{fontWeight: 'bold'}}>{wo.paymentStatus}</span>
                                    </Table.Cell>
                                </Table.Row>
                            </Table.Body>
                        ))}

                    </Table>
                </Aux>
        }

        let refusedOrders = undefined;

        if (this.props.ordersByMonth.filter(o => o.orderStatus === 'REFUSED')
            .length > 0) {

            refusedOrders =
                <Aux>
                    <Table color='red' size='small' collapsing compact>
                        <Label ribbon color='red' size='tiny'>Refused Orders</Label>
                        {header}
                        {this.props.ordersByMonth.filter(o => o.orderStatus === 'REFUSED').map((wo, index) => (
                            <Table.Body key={index}>
                                <Table.Row>
                                    <Table.Cell selectable>
                                        <span className="orders-id-text"
                                              onClick={() => this.handleOrder(wo.orderKey.orderId)}>{wo.orderKey.orderId}</span>
                                    </Table.Cell>
                                    <Table.Cell>{wo.orderKey.userEmail}</Table.Cell>
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
                                    <Table.Cell singleLine>{wo.orderKey.orderTime}</Table.Cell>
                                    <Table.Cell style={{backgroundColor: 'hsl(358, 100%, 63%)'}}>
                                        <span style={{fontWeight: 'bold'}}>{wo.orderStatus}</span>
                                    </Table.Cell>
                                    <Table.Cell>
                                        <span style={{fontWeight: 'bold'}}>{wo.paymentStatus}</span>
                                    </Table.Cell>
                                </Table.Row>
                            </Table.Body>
                        ))}

                    </Table>
                </Aux>
        }


        return (
            <div>
                <div className="table-orders-per-month">
                    {waitingOrders}
                    {requireActionsOrders}
                    {confirmedOrders}
                    {processingOrders}
                    {refusedOrders}
                </div>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        ordersByMonth: state.orders.ordersByMonth,
    }
}

const dispatchToProps = dispatch => {
    return {
        getOrderById: (orderId, history) => dispatch(actions.orderById(orderId, history)),
    }
}


export default connect(mapStateToProps, dispatchToProps)(OrdersGridByMonth);