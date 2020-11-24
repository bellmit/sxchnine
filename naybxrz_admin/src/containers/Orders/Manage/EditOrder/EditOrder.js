import React,{PureComponent} from "react";
import {connect} from 'react-redux';
import {Button, Dropdown, Form, Grid, Icon, Image, Label, Modal, Segment} from "semantic-ui-react";
import trash from "../../trash.png";
import * as actions from "../../../../store/actions";
import moment from "moment";

class EditOrder extends PureComponent {

    state = {
        orderStatus: this.props.orderById.orderStatus,
        shippingStatus: this.props.orderById.shippingStatus,
        shippingTime: this.props.orderById.shippingTime, //!== null ? moment(this.props.orderById.shippingTime).format("YYYY-MM-DD") : null,
        trackingNumber: this.props.orderById.trackingNumber,
        products: this.props.orderById.products,
        showDeleteButton: false
    }

    refreshState = () => {
        this.setState({
            orderStatus: this.props.orderById.orderStatus,
            shippingStatus: this.props.orderById.shippingStatus,
            shippingTime: this.props.orderById.shippingTime,// !== null ? moment(this.props.orderById.shippingTime).format("YYYY-MM-DD") : null,
            trackingNumber: this.props.orderById.trackingNumber,
            products: this.props.orderById.products
        });
    }


    handleChange = (e, {name, value}) => this.setState({[name]: value});

    closeModal = () => this.props.closeOrderByIdPopup(false);

    handleChangeOrderStatus = (e, {value}) => this.setState({value, orderStatus: value});

    handleChangeShippingStatus = (e, {value}) => this.setState({value, shippingStatus: value});

    showDeleteButton = () => this.setState({showDeleteButton: !this.state.showDeleteButton});

    removeProduct = (id) => {
        let products = [...this.state.products];
        products.splice(id, 1);
        this.setState({products: products});
    }

    saveCurrentOrder = () => {
        console.log(this.createOrder());
        this.props.saveOrder(this.createOrder());
    }

    createOrder(){
        return {
            orderKey: {
                orderId: this.props.orderById.orderKey.orderId,
                userEmail: this.props.orderById.orderKey.userEmail,
                orderTime: this.props.orderById.orderKey.orderTime
            },
            orderStatus: this.state.orderStatus,
            total: this.props.orderById.total,
            paymentStatus: this.props.orderById.paymentStatus,
            paymentTime: this.props.orderById.paymentTime,
            shippingStatus: this.state.shippingStatus,
            shippingTime: this.state.shippingTime !== null ? moment(this.state.shippingTime).format("YYYY-MM-DD hh:mm:ss"): null,
            trackingNumber: this.state.trackingNumber,
            products: this.state.products,
            paymentInfo: this.props.orderById.paymentInfo,
            userAddress: this.props.orderById.userAddress
        }
    }

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

        const orders = [
            { key: 0, text: '----', value: '' },
            { key: 1, text: 'WAITING', value: 'WAITING' },
            { key: 2, text: 'REQUIRED_ACTION', value: 'REQUIRED_ACTION' },
            { key: 3, text: 'CONFIRMED', value: 'CONFIRMED' },
            { key: 4, text: 'PROCESSING', value: 'PROCESSING' },
            { key: 5, text: 'SHIPPED', value: 'SHIPPED' },
            { key: 6, text: 'REFUSED', value: 'REFUSED' },
        ]

        const shippingStatus = [
            { key: 0, text: '----', value: '' },
            { key: 1, text: 'PENDING', value: 'PENDING' },
            { key: 2, text: 'SHIPPED', value: 'SHIPPED' },
        ]

        return (

            <Modal open={this.props.orderByIdPopup}
                   onClose={this.closeModal}
                   size='large' onOpen={this.refreshState} onMount={this.refreshState}>
                <Modal.Content>
                    <Segment.Group>
                        <Segment>
                            <Icon name='archive' />
                            <span style={{fontWeight: 'bold'}}>Order Info</span>
                        </Segment>
                        <Segment.Group>
                            <Segment>
                                <Grid>
                                    <Grid.Row>
                                        <Grid.Column width='3'>
                                            <span style={{fontWeight: 'bold', fontSize: 'small'}}>Order ID:</span>
                                        </Grid.Column>
                                        <Grid.Column width='3'>
                                            <span className="order-edit-text">{this.props.orderById.orderKey.orderId}</span>
                                        </Grid.Column>
                                        <Grid.Column width='3'>
                                            <span style={{fontWeight: 'bold', fontSize: 'small'}}>Order Time:</span>
                                        </Grid.Column>
                                        <Grid.Column width='3'>
                                            <span className="order-edit-text">{this.props.orderById.orderKey.orderTime}</span>
                                        </Grid.Column>
                                    </Grid.Row>
                                    <Grid.Row>
                                        <Grid.Column width='3'>
                                            <span style={{fontWeight: 'bold', fontSize: 'small'}}>User:</span>
                                        </Grid.Column>
                                        <Grid.Column width='3'>
                                            <span className="order-edit-text">{this.props.orderById.orderKey.userEmail}</span>
                                        </Grid.Column>
                                        <Grid.Column width='3'>
                                                <span style={{
                                                    fontWeight: 'bold',
                                                    fontSize: 'small'
                                                }}>Payment Intent ID:</span>
                                        </Grid.Column>
                                        <Grid.Column width='3'>
                                                <span
                                                    className="order-edit-text">{this.props.orderById.paymentInfo.paymentIntentId}</span>
                                        </Grid.Column>
                                    </Grid.Row>
                                </Grid>
                            </Segment>
                            <Segment.Group>
                                <Segment>
                                    <Icon name='address card'/><span
                                    style={{fontWeight: 'bold'}}>User Address</span>
                                </Segment>
                                <Segment>
                                    <Grid>
                                        <Grid.Row>
                                            <Grid.Column width='2'>
                                                    <span
                                                        style={{fontWeight: 'bold', fontSize: 'small'}}>Address:</span>
                                            </Grid.Column>
                                            <Grid.Column width='2'>
                                                    <span
                                                        className="order-edit-text">{this.props.orderById.userAddress.address}</span>
                                            </Grid.Column>
                                            <Grid.Column width='2'>
                                                    <span style={{
                                                        fontWeight: 'bold',
                                                        fontSize: 'small'
                                                    }}>Postal Code:</span>
                                            </Grid.Column>
                                            <Grid.Column width='2'>
                                                    <span
                                                        className="order-edit-text">{this.props.orderById.userAddress.postalCode}</span>
                                            </Grid.Column>
                                        </Grid.Row>
                                        <Grid.Row>
                                            <Grid.Column width='2'>
                                                <span style={{fontWeight: 'bold', fontSize: 'small'}}>City:</span>
                                            </Grid.Column>
                                            <Grid.Column width='2'>
                                                <span className="order-edit-text">{this.props.orderById.userAddress.province} {this.props.orderById.userAddress.city}</span>
                                            </Grid.Column>
                                            <Grid.Column width='2'>
                                                <span style={{fontWeight: 'bold', fontSize: 'small'}}>Country:</span>
                                            </Grid.Column>
                                            <Grid.Column width='2'>
                                                    <span
                                                        className="order-edit-text">{this.props.orderById.userAddress.country}</span>
                                            </Grid.Column>
                                        </Grid.Row>
                                    </Grid>
                                </Segment>
                            </Segment.Group>
                            <Segment>
                                <Grid>
                                    <Grid.Row>
                                        <Grid.Column width='3'>
                                            <span style={{fontWeight: 'bold', fontSize: 'small'}}>Payment Status:</span>
                                        </Grid.Column>
                                        <Grid.Column width='3'>
                                            <span className="order-edit-text">{this.props.orderById.paymentStatus}</span>
                                        </Grid.Column>
                                        <Grid.Column width='3'>
                                            <span style={{fontWeight: 'bold', fontSize: 'small'}}>Payment Time:</span>
                                        </Grid.Column>
                                        <Grid.Column width='3'>
                                            <span className="order-edit-text">{this.props.orderById.paymentTime}</span>
                                        </Grid.Column>
                                    </Grid.Row>
                                    <Grid.Row>
                                        <Grid.Column width='3'>
                                            <span style={{fontWeight: 'bold', fontSize: 'small'}}>Total:</span>
                                        </Grid.Column>
                                        <Grid.Column width='3'>
                                            <Label tag color='red' size='mini'>${this.props.orderById.total}</Label>
                                        </Grid.Column>
                                        <Grid.Column width='3'>
                                            <span style={{fontWeight: 'bold', fontSize: 'small'}}>Order Status:</span>
                                        </Grid.Column>
                                        <Grid.Column width='3'>
                                            <span style={{fontWeight: 'bold',
                                                fontSize: 'small',
                                                backgroundColor: this.getColor(this.state.orderStatus)}}>
                                                {this.state.orderStatus}
                                            </span>
                                            <Dropdown className="order-edit-text"
                                                      onChange={this.handleChangeOrderStatus}
                                                      placeholder='Order Status'
                                                      text='Order Status'
                                                      options={orders}
                                                      selection
                                                      value={this.state.orderStatus}/>
                                        </Grid.Column>
                                    </Grid.Row>
                                </Grid>
                            </Segment>
                        </Segment.Group>
                        <Segment.Group>
                            <Segment>
                                <Icon name='shipping' /><span style={{fontWeight: 'bold'}}>Shipping</span>
                            </Segment>
                            <Segment.Group>
                                <Segment>
                                    <Grid>
                                        <Grid.Row>
                                            <Grid.Column width='3'>
                                                <span style={{fontWeight: 'bold', fontSize: 'small'}}>Shipping Status:</span>
                                            </Grid.Column>
                                            <Grid.Column width='3'>
                                                <span className="order-edit-text">{this.state.shippingStatus}</span>
                                                <Dropdown className="order-edit-text"
                                                          onChange={this.handleChangeShippingStatus}
                                                          placeholder='Shipping Status'
                                                          text='Shipping Status'
                                                          options={shippingStatus}
                                                          selection
                                                          value={this.state.shippingStatus}/>
                                            </Grid.Column>
                                            <Grid.Column width='3'>
                                                <span style={{fontWeight: 'bold', fontSize: 'small'}}>Shipping Time:</span>
                                            </Grid.Column>
                                            <Grid.Column>
                                                <Form.Input name = 'shippingTime'
                                                            size='small'
                                                            type= 'date'
                                                            value={this.state.shippingTime !== null ? this.state.shippingTime : '1900-01-01'}
                                                            onChange={this.handleChange} />
                                            </Grid.Column>
                                        </Grid.Row>
                                        <Grid.Row>
                                            <Grid.Column width='3'>
                                                <span style={{fontWeight: 'bold', fontSize: 'small'}}>Tracking Number:</span>
                                            </Grid.Column>
                                            <Grid.Column>
                                                <Form.Input name = 'trackingNumber' size='small'
                                                            value={this.state.trackingNumber}
                                                            onChange={this.handleChange} />
                                            </Grid.Column>
                                        </Grid.Row>
                                    </Grid>
                                </Segment>
                            </Segment.Group>
                        </Segment.Group>
                        <Segment.Group>
                            <Segment>
                                <Icon name='barcode'/><span style={{fontWeight: 'bold'}}>Products</span>
                            </Segment>
                            <Segment.Group>
                                <Segment>
                                    <Grid className="Grid-div">
                                        <Label attached="top right"
                                               color='red'
                                               className="order-products-show"
                                               onClick={this.showDeleteButton}>Edit Products?</Label>
                                        {this.state.products.map((product, index) => (
                                            <Grid.Row key={index}>
                                                <Grid.Column width={4}>
                                                    <Image wrapped
                                                           size='small'
                                                           src={product.image}/>
                                                </Grid.Column>
                                                <Grid.Column width={8}>
                                                    <span style={{fontWeight: 'bold', fontSize: 'small'}}>{product.productName}</span>
                                                    <p style={{fontWeight: 'bold', fontSize: 'small'}}>{product.productColor}</p>
                                                    <p style={{fontWeight: 'bold', fontSize: 'small'}}>{product.productSize}</p>
                                                    <p style={{fontWeight: 'bold', fontSize: 'small'}}>${product.unitPrice}</p>
                                                </Grid.Column>
                                                {this.state.showDeleteButton &&
                                                <Grid.Column>
                                                    <img alt=""
                                                         src={trash}
                                                         className="trash-icon"
                                                         onClick={() => this.removeProduct(index)}/>
                                                </Grid.Column>}
                                            </Grid.Row>
                                        ))}
                                    </Grid>
                                </Segment>
                            </Segment.Group>
                        </Segment.Group>
                    </Segment.Group>

                    <Modal.Actions>
                        <Button className="order-save-button"
                                color='black'
                                floated='right'
                                onClick={this.saveCurrentOrder}>
                            <span className="order-save-button-text">SAVE</span>
                            <Icon name='right chevron' color='yellow'/>
                        </Button>
                    </Modal.Actions>
                </Modal.Content>
            </Modal>
        );
    }
}

const mapStateToProps = state => {
    return {
        orderByIdPopup: state.orders.orderByIdPopup,
        orderById: state.orders.orderById,
        saveOrderError: state.orders.saveOrderError
    }
}

const dispatchToProps = dispatch => {
    return {
        getOrderById: (orderId) => dispatch(actions.orderById(orderId)),
        closeOrderByIdPopup: (open) => dispatch(actions.orderByIdPopup(open)),
        saveOrder: (order) => dispatch(actions.saveOrder(order))
    }
}

export default connect(mapStateToProps, dispatchToProps)(EditOrder);