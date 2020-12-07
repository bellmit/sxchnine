import React, {PureComponent} from 'react';
import {connect} from 'react-redux';
import {Tabs, Tab, TabPanel, TabList} from 'react-web-tabs';
import {Dimmer, Form, Grid, Image, Label, Loader, Modal, Progress} from "semantic-ui-react";
import Aux from "../../hoc/Aux/Aux";
import 'react-web-tabs/dist/react-web-tabs.css';
import './TabAccount.css';
import grunge from './grunge.png';
import pencil from './pencil.png';
import * as actions from "../../store/actions";

class TabAccount extends PureComponent {

    state = {
        open: false,
        email: this.props.user.email,
        phone: this.props.user.phone,
        number: this.props.user.address.number,
        address: this.props.user.address.address,
        postalCode: this.props.user.address.postalCode,
        city: this.props.user.address.city,
        country: this.props.user.address.country,
        oldPassword: '',
        newPassword: '',
        confirmNewPassword: '',
        errorOldPassword: '',
        flagErrorOldPassword: false,
        errorNewPassword: '',
        flagErrorNewPassword: false,
        errorConfirmPassword: '',
        flagErrorConfirmPassword: false
    }

    show = () => this.setState({open: true});
    close = () => this.setState({open: false});
    handleChange = (e, {name, value}) => this.setState({[name]: value});

    statusOrder = (status) => {
        if (status === 'ORDERED' || status === 'REQUIRED_ACTION' || status === 'WAITING')
            return 20;
        else if (status === 'CONFIRMED')
            return 50;
        else if (status === 'PROCESSING')
            return 79;
        else if (status === 'SHIPPED')
            return 100;
    };

    addUser = () => {
        console.log(this.constructUser());
        this.props.addUser(this.constructUser());
        this.close();
    };

    constructUser() {
        return {
            id: this.props.user.id,
            firstName: this.props.user.firstName,
            lastName: this.props.user.lastName,
            email: this.state.email,
            phoneNumber: this.state.phone,
            address: {
                number: this.state.number,
                address: this.state.address,
                city: this.state.city,
                postalCode: this.state.postalCode,
                country: this.state.country
            }
        };
    }

    changePassword = () => {
        if (this.state.oldPassword !== '' && this.state.newPassword !== '' && this.state.confirmNewPassword !== '') {
            this.props.changedPassword(this.props.user.email, this.state.oldPassword, this.state.newPassword, this.state.confirmNewPassword);
        }

        if (this.state.oldPassword === '') {
            this.setState({errorOldPassword: 'Missing Old Password !'});
            this.setState({flagErrorOldPassword: true});
        } else {
            this.setState({flagErrorOldPassword: false});
        }

        if (this.state.newPassword === '') {
            this.setState({errorNewPassword: 'Missing New Password !'});
            this.setState({flagErrorNewPassword: true});
        } else {
            this.setState({flagErrorNewPassword: false});
        }

        if (this.state.confirmNewPassword === '') {
            this.setState({errorConfirmNewPassword: 'Missing Confirm Password !'});
            this.setState({flagErrorConfirmNewPassword: true});
        } else {
            this.setState({flagErrorConfirmNewPassword: false});
        }

    }

    componentDidUpdate(prevProps: Readonly<P>, prevState: Readonly<S>, snapshot: SS) {
        if (this.props.userChangedPassword === true) {
            this.setState({oldPassword: ''});
            this.setState({newPassword: ''});
            this.setState({confirmNewPassword: ''});
        }
    }


    render() {
        const {open} = this.state;

        let orderInprogress = <Aux>
            <Label color="red" attached="top right">No order in progress with us for now ... Start picking before is too
                late -> Go Got it !</Label>
        </Aux>

        if (this.props.ordersHistory
            .filter(o => o.orderStatus !== 'SHIPPED')
            .length > 0) {
            orderInprogress = this.props.ordersHistory
                .filter(o => o.orderStatus !== 'SHIPPED')
                .map((order, index) => (
                    <Grid key={index}>
                        <Grid.Row>
                            <Grid.Column width={5}>
                                <span className="TabAccount-Message">
                                    Order ID: {order.orderKey.orderId}
                                </span>
                            </Grid.Column>
                            <Grid.Column width={5}>
                                <span className="TabAccount-Message">
                                    Order time: {order.orderKey.orderTime}
                                </span>
                            </Grid.Column>
                            <Grid.Column width={5}>
                                <span>
                                    <Label tag color='red'>${order.total}</Label>
                                </span>
                            </Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <Grid.Column width={5}>
                                <span className="TabAccount-Message">
                                    Order Status:
                                </span>
                            </Grid.Column>
                            <Grid.Column width={6}>
                                {order.orderStatus === 'REFUSED' ? <Progress size='small' percent={100} error indicating> Refused </Progress>: <Progress size='small' percent={this.statusOrder(order.orderStatus)} indicating>
                                        <span className="TabAccount-Progress-Text">ordered&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            processing&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            preparing to ship&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            shipped</span></Progress>}
                            </Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <Grid container>
                                {order.products.map((product, indexProduct) => (
                                    <Grid.Row key={indexProduct}>
                                        <Grid.Column width={5} style={{left: '40px'}}>
                                            <Image wrapped
                                                   size='small'
                                                   src={product.image}/>
                                        </Grid.Column>
                                        <Grid.Column width={5} style={{left: '40px'}}>
                                            <p className="TabAccount-Message">
                                                {product.productName}
                                            </p>
                                            <p className="TabAccount-Message">
                                                {product.productColor}
                                            </p>
                                            <p className="TabAccount-Message">
                                                {product.productSize}
                                            </p>
                                            <p className="TabAccount-Message">
                                                ${product.unitPrice}
                                            </p>
                                        </Grid.Column>
                                    </Grid.Row>
                                ))}
                            </Grid>
                        </Grid.Row>
                    </Grid>
                ))
        }

        let orderConfirmed = <Aux>
            <Label color="red" attached="top right">No history with us for now ... Start picking before is too late ->
                Go Got it !</Label>
        </Aux>

        if (this.props.ordersHistory
            .filter(o => o.orderStatus === 'SHIPPED')
            .length > 0) {
            orderConfirmed = this.props.ordersHistory
                .filter(o => o.orderStatus === 'SHIPPED')
                .map((order, index) => (
                    <Grid key={index}>
                        <Grid.Row>
                            <Grid.Column width={5}>
                                <span className="TabAccount-Message">
                                    Order ID: {order.orderKey.orderId}
                                </span>
                            </Grid.Column>
                            <Grid.Column width={5}>
                                <span className="TabAccount-Message">
                                    Order time: {order.orderKey.orderTime}
                                </span>
                            </Grid.Column>
                            <Grid.Column width={5}>
                                <span>
                                    <Label tag color='red'>${order.total}</Label>
                                </span>
                            </Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <Grid.Column width={5}>
                                <span className="TabAccount-Message">
                                    Order Status:
                                </span>
                            </Grid.Column>
                            <Grid.Column width={6}>
                                {order.orderStatus === 'REFUSED' ? <Progress size='small' percent={100} error indicating> Refused </Progress>: <Progress size='small' percent={this.statusOrder(order.orderStatus)} indicating>
                                        <span className="TabAccount-Progress-Text">ordered&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        processing&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        preparing to ship&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        shipped</span></Progress>}
                            </Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <Grid container>
                                {order.products.map((product, indexProduct) => (
                                    <Grid.Row key={indexProduct}>
                                        <Grid.Column width={5} style={{left: '40px'}}>
                                            <Image wrapped
                                                   size='small'
                                                   src={product.image}/>
                                        </Grid.Column>
                                        <Grid.Column width={5} style={{left: '40px'}}>
                                            <p className="TabAccount-Message">
                                                {product.productName}
                                            </p>
                                            <p className="TabAccount-Message">
                                                {product.productColor}
                                            </p>
                                            <p className="TabAccount-Message">
                                                {product.productSize}
                                            </p>
                                            <p className="TabAccount-Message">
                                                ${product.unitPrice}
                                            </p>
                                        </Grid.Column>
                                    </Grid.Row>
                                ))}
                            </Grid>
                        </Grid.Row>
                    </Grid>
                ))
        }

        let changedPasswordLabel = '';

        if (this.props.userChangedPassword === true) {
            changedPasswordLabel = <Label color="green">- Password changed successfully -</Label>
        }

        if (this.props.errorChangedPassword) {
            changedPasswordLabel = <Label color="red">{this.props.errorChangedPassword}</Label>
        }


        return (
            <Aux>
                <Tabs defaultTab="vertical-tab-one" vertical>
                    <TabList>
                        <Tab tabFor="vertical-tab-one">
                            <span style={{fontFamily: 'Anton', fontSize: '1vw'}}>Order in progress..</span>
                        </Tab>
                        <Tab tabFor="vertical-tab-two">
                            <span style={{fontFamily: 'Anton', fontSize: '1vw'}}>Order History</span>
                        </Tab>
                        <Tab tabFor="vertical-tab-three">
                            <span style={{fontFamily: 'Anton', fontSize: '1vw'}}>Personal Info</span>
                        </Tab>
                        <Tab tabFor="vertical-tab-four">
                            <span style={{fontFamily: 'Anton', fontSize: '1vw'}}>Change password</span>
                        </Tab>
                    </TabList>
                    <TabPanel tabId="vertical-tab-one">
                        <Grid>
                            {orderInprogress}
                        </Grid>
                    </TabPanel>
                    <TabPanel tabId="vertical-tab-two">
                        <Grid>
                            {orderConfirmed}
                        </Grid>
                    </TabPanel>
                    <TabPanel tabId="vertical-tab-three">
                        <Grid>
                            <Grid.Row>
                                <Grid.Column width={9} className="TabAccount-Grid">
                                    <Grid>
                                        <Grid.Row>
                                            <Grid.Column width={5}>
                                                <span className="TabAccount-Message">
                                                    First name:
                                                </span>
                                            </Grid.Column>
                                            <Grid.Column width={5}>
                                                <span className="TabAccount-Message">
                                                    {this.props.user.firstName}
                                                </span>
                                            </Grid.Column>
                                        </Grid.Row>
                                        <Grid.Row>
                                            <Grid.Column width={5}>
                                                <span className="TabAccount-Message">
                                                    Last name:
                                                </span>
                                            </Grid.Column>
                                            <Grid.Column width={5}>
                                                <span className="TabAccount-Message">
                                                    {this.props.user.lastName}
                                                </span>
                                            </Grid.Column>
                                        </Grid.Row>
                                        <Grid.Row>
                                            <Grid.Column width={5}>
                                                <span className="TabAccount-Message">
                                                    Email:
                                                </span>
                                            </Grid.Column>
                                            <Grid.Column width={5}>
                                                <span className="TabAccount-Message">
                                                    {this.props.user.email}
                                                </span>
                                            </Grid.Column>
                                        </Grid.Row>
                                        <Grid.Row>
                                            <Grid.Column width={5}>
                                                <span className="TabAccount-Message">
                                                    Phone:
                                                </span>
                                            </Grid.Column>
                                            <Grid.Column width={5}>
                                                <span className="TabAccount-Message">
                                                    {this.props.user.phoneNumber}
                                                </span>
                                            </Grid.Column>
                                        </Grid.Row>
                                        <Grid.Row>
                                            <Grid.Column width={5}>
                                                <span className="TabAccount-Message">
                                                    Address:
                                                </span>
                                            </Grid.Column>
                                            <Grid.Column width={8}>
                                                <span className="TabAccount-Message">
                                                    {this.props.user.address.number} {this.props.user.address.address}
                                                </span>
                                                <p className="TabAccount-Message">
                                                    {this.props.user.address.city} {this.props.user.address.postalCode} {this.props.user.address.country}
                                                </p>
                                            </Grid.Column>
                                        </Grid.Row>
                                    </Grid>
                                </Grid.Column>
                                <Grid.Column width={3}>
                                    <img src={grunge} alt="edit" className="TabAccount-EditUser-Icon"
                                         onClick={this.show}/>
                                    <img src={pencil} alt="edit" className="TabAccount-EditUser-Icon"
                                         onClick={this.show}/>
                                </Grid.Column>
                            </Grid.Row>
                        </Grid>
                    </TabPanel>
                    <TabPanel tabId="vertical-tab-four">
                        <Grid>
                            <Grid.Row>
                                <Grid.Column width={5}>
                                    <span className="TabAccount-Message">
                                        Old password:
                                    </span>
                                </Grid.Column>
                                <Grid.Column width={6}>
                                    <Form inverted>
                                        <Form.Input placeholder="Old password..."
                                                    type="password"
                                                    name='oldPassword'
                                                    error={this.state.flagErrorOldPassword && this.state.errorOldPassword}
                                                    value={this.state.oldPassword}
                                                    onChange={this.handleChange}/>
                                    </Form>
                                </Grid.Column>
                            </Grid.Row>
                            <Grid.Row>
                                <Grid.Column width={5}>
                                    <span className="TabAccount-Message">
                                        New password:
                                    </span>
                                </Grid.Column>
                                <Grid.Column width={6}>
                                    <Form inverted>
                                        <Form.Input placeholder="New password..."
                                                    type="password"
                                                    name='newPassword'
                                                    error={this.state.flagErrorNewPassword && this.state.errorNewPassword}
                                                    value={this.state.newPassword}
                                                    onChange={this.handleChange}/>
                                    </Form>
                                </Grid.Column>
                            </Grid.Row>
                            <Grid.Row>
                                <Grid.Column width={5}>
                                    <span className="TabAccount-Message">
                                        Confirm password:
                                    </span>
                                </Grid.Column>
                                <Grid.Column width={6}>
                                    <Form inverted>
                                        <Form.Input placeholder="Confirm password..."
                                                    type="password"
                                                    name='confirmNewPassword'
                                                    error={this.state.flagErrorConfirmNewPassword && this.state.errorConfirmNewPassword}
                                                    value={this.state.confirmNewPassword}
                                                    onChange={this.handleChange}/>
                                    </Form>
                                </Grid.Column>
                            </Grid.Row>
                            <Grid.Row centered>
                                {changedPasswordLabel}
                            </Grid.Row>
                            <Grid.Row centered>
                                <button className="TabAccount-Edit-Button" onClick={this.changePassword}>
                                    <span className="TabAccount-Text-Edit-Button">Change Password</span>
                                </button>
                            </Grid.Row>
                        </Grid>
                    </TabPanel>
                </Tabs>


                <Modal open={open} onClose={this.close}
                       style={{position: 'static', height: 'auto', width: '50%'}} closeIcon>

                    <Dimmer active={this.props.loading} page>
                        <Loader content='Loading'/>
                    </Dimmer>
                    <Modal.Content>
                        <Grid centered>
                            <Grid.Row>
                                <Grid.Column width={5}>
                                    <span className="TabAccount-Message">
                                        Email:
                                    </span>
                                </Grid.Column>
                                <Grid.Column width={5}>
                                    <Form.Input inverted
                                                placeholder="Email..."
                                                name='email'
                                                value={this.state.email}
                                                onChange={this.handleChange}/>
                                </Grid.Column>
                            </Grid.Row>

                            <Grid.Row>
                                <Grid.Column width={5}>
                                    <span className="TabAccount-Message">
                                        Phone:
                                    </span>
                                </Grid.Column>
                                <Grid.Column width={5}>
                                    <Form.Input inverted
                                                placeholder="Phone..."
                                                name='phone'
                                                value={this.state.phone}
                                                onChange={this.handleChange}/>
                                </Grid.Column>
                            </Grid.Row>
                            <Grid.Row>
                                <Grid.Column width={5}>
                                    <span className="TabAccount-Message">
                                        Address:
                                    </span>
                                </Grid.Column>
                                <Grid.Column width={5}>
                                    <Form.Input inverted
                                                placeholder='N°'
                                                name='number'
                                                value={this.state.number}
                                                onChange={this.handleChange}/>

                                    <Form.Input inverted
                                                placeholder='Street/Avenue'
                                                name='address'
                                                value={this.state.address}
                                                onChange={this.handleChange}/>
                                    <Form.Input inverted
                                                placeholder='City'
                                                name='city'
                                                value={this.state.city}
                                                onChange={this.handleChange}/>
                                    <Form.Input inverted
                                                placeholder='Postal Code'
                                                name='postalCode'
                                                value={this.state.postalCode}
                                                onChange={this.handleChange}/>
                                    <Form.Input inverted
                                                placeholder='Country'
                                                name='country'
                                                value={this.state.country}
                                                onChange={this.handleChange}/>
                                </Grid.Column>
                            </Grid.Row>
                        </Grid>
                    </Modal.Content>
                    <Modal.Actions>
                        <button className="TabAccount-Edit-Button" onClick={this.addUser}>
                            <span className="TabAccount-Text-Edit-Button">Edit Info</span>
                        </button>
                    </Modal.Actions>

                </Modal>
            </Aux>
        );
    }
}

const mapStateToProps = state => {
    return {
        user: state.users.userAuthenticated,
        ordersHistory: state.order.ordersHistory,
        loading: state.users.loading,
        errorChangedPassword: state.users.errorChangedPassword,
        userChangedPassword: state.users.userChangedPassword,
        addedUser: state.users.addedUser
    }
}

const dispatchToProps = dispatch => {
    return {
        addUser: (userToAdd) => dispatch(actions.saveUser(userToAdd)),
        changedPassword: (email, oldPassword, newPassword, confirmNewPassword) => dispatch(actions.changePassword(email, oldPassword, newPassword, confirmNewPassword))
    }
}


export default connect(mapStateToProps, dispatchToProps)(TabAccount);