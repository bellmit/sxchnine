import React, {PureComponent} from "react";
import {connect} from 'react-redux';
import {Button, Dimmer, Form, Icon, Loader, Modal, Segment} from "semantic-ui-react";
import './FormUser.css';
import * as actions from "../../../store/actions";

class FormUser extends PureComponent {

    state = {
        id: this.props.getUserData !== undefined ? this.props.getUserData.id : '',
        email: this.props.getUserData !== undefined ? this.props.getUserData.email : '',
        firstName: this.props.getUserData !== undefined ? this.props.getUserData.firstName : '',
        lastName: this.props.getUserData !== undefined ? this.props.getUserData.lastName : '',
        gender: this.props.getUserData !== undefined ? this.props.getUserData.gender : '',
        phoneNumber: this.props.getUserData !== undefined ? this.props.getUserData.phoneNumber : '',
        password: this.props.getUserData !== undefined ? this.props.getUserData.password : '',
        number: this.props.getUserData !== undefined ? this.props.getUserData.address.number : '',
        address: this.props.getUserData !== undefined ? this.props.getUserData.address.address : '',
        city: this.props.getUserData !== undefined ? this.props.getUserData.address.city : '',
        postalCode: this.props.getUserData !== undefined ? this.props.getUserData.address.postalCode : '',
        province: this.props.getUserData !== undefined ? this.props.getUserData.address.province : '',
        country: this.props.getUserData !== undefined ? this.props.getUserData.address.country : '',
        role: this.props.getUserData !== undefined ? this.props.getUserData.role : '',
        status: this.props.getUserData !== undefined ? this.props.getUserData.status : ''
    }

    refreshState = () => {
        this.setState({
            id: this.props.getUserData !== undefined ? this.props.getUserData.id : '',
            email: this.props.getUserData !== undefined ? this.props.getUserData.email : '',
            firstName: this.props.getUserData !== undefined ? this.props.getUserData.firstName : '',
            lastName: this.props.getUserData !== undefined ? this.props.getUserData.lastName : '',
            gender: this.props.getUserData !== undefined ? this.props.getUserData.gender : '',
            phoneNumber: this.props.getUserData !== undefined ? this.props.getUserData.phoneNumber : '',
            password: this.props.getUserData !== undefined ? this.props.getUserData.password : '',
            number: this.props.getUserData !== undefined ? this.props.getUserData.address.number : '',
            address: this.props.getUserData !== undefined ? this.props.getUserData.address.address : '',
            city: this.props.getUserData !== undefined ? this.props.getUserData.address.city : '',
            postalCode: this.props.getUserData !== undefined ? this.props.getUserData.address.postalCode : '',
            province: this.props.getUserData !== undefined ? this.props.getUserData.address.province : '',
            country: this.props.getUserData !== undefined ? this.props.getUserData.address.country : '',
            role: this.props.getUserData !== undefined ? this.props.getUserData.role : '',
            status: this.props.getUserData !== undefined ? this.props.getUserData.status : ''
        })
    }

    handleChange = (e, {name, value}) => this.setState({[name]: value});

    handleChangeGender = (e, {value}) => this.setState({value, gender: value});

    handleChangeRole = (e, {value}) => this.setState({value, role: value});

    handleChangeStatus = (e, {value}) => this.setState({value, status: value});

    closeModal = () => this.props.closeUserPopup(this.props.history);

    saveUser = () => {
        if (!this.props.editMode){
            this.props.saveUser(this.createUser(), this.props.history, true);
        } else {
            this.props.saveUser(this.createUser(), this.props.history, false);
        }
    }

    createUser() {
        return {
            id: !this.props.editMode ? this.generateId() : this.state.id,
            email: this.state.email,
            firstName: this.state.firstName,
            lastName: this.state.lastName,
            gender: this.state.gender,
            phoneNumber: this.state.phoneNumber,
            password: this.state.password,
            address: {
                number: this.state.number,
                address: this.state.address,
                city: this.state.city,
                postalCode: this.state.postalCode,
                province: this.state.province,
                country: this.state.country,
            },
            role: this.state.role,
            status: this.state.status
        }
    }

    generateId(){
        return (Date.now().toString(10) + Math.random().toString(2).substr(2, 5)).toUpperCase();
    }


    render() {

        let optionsGender = [
            {key: 0, text: '-----', value: 'NA'},
            {key: 1, text: 'Man', value: 'M'},
            {key: 2, text: 'Woman', value: 'W'}
        ];

        let optionsRole = [
            {key: 1, text: 'ADMIN', value: 'ADMIN'},
            {key: 2, text: 'NORMAL', value: 'NORMAL'},
            {key: 3, text: 'GUEST', value: 'GUEST'}
        ];

        let optionsStatus = [
            {key: 1, text: 'NA', value: null},
            {key: 2, text: 'ACTIF', value: true},
            {key: 3, text: 'INACTIF', value: false}
        ];

        return (
            <Modal open={this.props.getUserPopup}
                   onClose={this.closeModal}
                   size='large' onOpen={this.refreshState} onMount={this.refreshState}>
                <Dimmer active={this.props.loading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                <Modal.Content>
                    <Segment.Group>
                        <Segment>
                            <Icon name='user circle'/>
                            <span style={{fontWeight: 'bold'}}>User Info</span>
                        </Segment>
                        <Segment.Group>
                            <Segment>
                                <Form>
                                    <Form.Group widths='equal'>
                                        <Form.Input fluid
                                                    label='User ID:'
                                                    name='id'
                                                    value={this.state.id}
                                                    disabled
                                                    inverted
                                                    onChange={this.handleChange}/>
                                        <Form.Input fluid
                                                    label='Email:'
                                                    name='email'
                                                    value={this.state.email}
                                                    inverted
                                                    onChange={this.handleChange}/>
                                        <Form.Input fluid
                                                    label='Password:'
                                                    name='password'
                                                    type='password'
                                                    value={this.state.password}
                                                    inverted
                                                    onChange={this.handleChange}/>
                                        <Form.Dropdown options={optionsGender}
                                                       label="Gender:"
                                                       name='gender'
                                                       value={this.state.gender}
                                                       onChange={this.handleChangeGender}/>
                                    </Form.Group>
                                    <Form.Group widths='equal'>
                                        <Form.Input fluid
                                                    label='First name:'
                                                    name='firstName'
                                                    value={this.state.firstName}
                                                    inverted
                                                    onChange={this.handleChange}/>
                                        <Form.Input fluid
                                                    label='Last name:'
                                                    name='lastName'
                                                    value={this.state.lastName}
                                                    inverted
                                                    onChange={this.handleChange}/>
                                        <Form.Input fluid
                                                    label='Phone Number:'
                                                    name='phoneNumber'
                                                    value={this.state.phoneNumber}
                                                    inverted
                                                    onChange={this.handleChange}/>
                                    </Form.Group>
                                </Form>
                            </Segment>
                        </Segment.Group>
                        <Segment.Group>
                            <Segment>
                                <Icon name='address book'/>
                                <span style={{fontWeight: 'bold'}}>User Address</span>
                            </Segment>
                            <Segment.Group>
                                <Segment>
                                    <Form>
                                        <Form.Group widths='equal' inline>
                                            <Form.Input fluid
                                                        label='Number:'
                                                        name='number'
                                                        value={this.state.number}
                                                        inverted
                                                        onChange={this.handleChange}/>
                                            <Form.Input fluid
                                                        label='Address:'
                                                        name='address'
                                                        value={this.state.address}
                                                        inverted
                                                        onChange={this.handleChange}/>
                                            <Form.Input fluid
                                                        label='City:'
                                                        name='city'
                                                        value={this.state.city}
                                                        inverted
                                                        onChange={this.handleChange}/>
                                            <Form.Input fluid
                                                        label='Postal Code:'
                                                        name='postalCode'
                                                        value={this.state.postalCode}
                                                        inverted
                                                        onChange={this.handleChange}/>
                                            <Form.Input fluid
                                                        label='Province:'
                                                        name='province'
                                                        value={this.state.province}
                                                        inverted
                                                        onChange={this.handleChange}/>
                                            <Form.Input fluid
                                                        label='Country:'
                                                        name='country'
                                                        value={this.state.country}
                                                        inverted
                                                        onChange={this.handleChange}/>
                                        </Form.Group>
                                    </Form>
                                </Segment>
                            </Segment.Group>
                        </Segment.Group>
                        <Segment.Group>
                            <Segment>
                                <Icon name='accessible'/>
                                <span style={{fontWeight: 'bold'}}>Access</span>
                            </Segment>
                            <Segment.Group>
                                <Form>
                                    <Form.Group widths='equal' inline>
                                        <Form.Dropdown options={optionsRole} basic
                                                       label="Role:"
                                                       name='role'
                                                       value={this.state.role}
                                                       onChange={this.handleChangeRole}/>
                                        <Form.Dropdown options={optionsStatus}
                                                       label="Status:"
                                                       name='status'
                                                       value={this.state.status}
                                                       onChange={this.handleChangeStatus}/>
                                    </Form.Group>
                                </Form>
                            </Segment.Group>
                        </Segment.Group>
                    </Segment.Group>
                    <Modal.Actions>
                        <Button className="user-save-button"
                                color='black'
                                floated='right'
                                onClick={this.saveUser}>
                            <span className="user-save-button-text">SAVE</span>
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
        getUserPopup: state.user.getUserPopup,
        saveUserError: state.user.saveUserError,
        loading: state.user.saveUserLoading
    }
}

const dispatchToProps = dispatch => {
    return {
        closeUserPopup: (history) => dispatch(actions.closeUserModalAndRedirectBack(history)),
        saveUser: (user, history, isNew) => dispatch(actions.saveUser(user, history, isNew))
    }
}

export default connect(mapStateToProps, dispatchToProps)(FormUser);