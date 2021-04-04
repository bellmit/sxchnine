import React, {Component} from 'react';
import Aux from '../../hoc/Aux/Aux';
import './Account.css';
import {Form, Modal, Grid, Label, Dimmer, Loader} from "semantic-ui-react";
import {connect} from 'react-redux';
import uuid from 'uuid/v1';
import * as actions from "../../store/actions";


class Account extends Component {

    state = {
        open: false,
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        num: '',
        avenue: '',
        city: '',
        postalCode: '',
        country: '',
        disabled: false
    };


    show = () => this.setState({open: true});
    close = () => this.setState({open: false});
    handleChange = (e, {name, value}) => this.setState({[name]: value});

    addUser = () => {
        this.props.addUser(this.constructUser());
        this.close();
    };

    constructUser() {
        return {
            id: uuid(),
            firstName: this.state.firstName,
            lastName: this.state.lastName,
            email: this.state.email,
            password: this.state.password,
            address: {
                number: this.state.num,
                address: this.state.avenue,
                city: this.state.city,
                postalCode: this.state.postalCode,
                country: this.state.country
            }

        };
    }

    render() {

        const {open} = this.state;

        return (
            <Aux>
                <div>
                    <Label as='a'
                           color='red'
                           size='mini'
                           basic
                           pointing
                           onClick={this.show}
                           className="Member-Text">Do you wanna be in?</Label>
                </div>

                <Dimmer active={this.props.loading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                <div>
                    <Modal open={open} onClose={this.close}
                           style={{position: 'static', height: 'auto', width: '50%'}} closeIcon>

                        <Modal.Content>
                            <Grid centered>
                                <Grid.Row>
                                    <Grid.Column width={5}>
                                        <Grid.Row>
                                            <Grid.Column width={3}>
                                                <Form.Input inverted required
                                                            placeholder='First Name...'
                                                            className="Info-Text"
                                                            name='firstName'
                                                            value={this.state.firstName}
                                                            onChange={this.handleChange}/>
                                            </Grid.Column>
                                        </Grid.Row>
                                        <Grid.Row>
                                            <Grid.Column width={3}>
                                                <Form.Input inverted
                                                            placeholder='Last Name...'
                                                            className="Info-Text"
                                                            name='lastName'
                                                            value={this.state.lastName}
                                                            onChange={this.handleChange}/>
                                            </Grid.Column>
                                        </Grid.Row>

                                        <Grid.Row>
                                            <Grid.Column width={3}>
                                                <Form.Input inverted
                                                            placeholder='Email Address...'
                                                            className="Info-Text"
                                                            name='email'
                                                            value={this.state.email}
                                                            onChange={this.handleChange}/>
                                            </Grid.Column>
                                        </Grid.Row>

                                        <Grid.Row>
                                            <Grid.Column width={3}>
                                                <Form.Input inverted
                                                            placeholder='Password'
                                                            name='password'
                                                            type='password'
                                                            className="Info-Text"
                                                            value={this.state.password}
                                                            onChange={this.handleChange}/>
                                            </Grid.Column>
                                        </Grid.Row>
                                    </Grid.Column>

                                    <Grid.Column>
                                        <div className="Account-Empty-Div"/>
                                    </Grid.Column>

                                    <Grid.Column width={5}>
                                        <Grid.Row>
                                            <Grid.Column width={3}>
                                                <Form.Input inverted placeholder='N°'
                                                            name='num'
                                                            className="Info-Text"
                                                            value={this.state.num}
                                                            onChange={this.handleChange}/>
                                                <Form.Input inverted
                                                            placeholder='Street/Avenue'
                                                            name='avenue'
                                                            className="Info-Text"
                                                            value={this.state.avenue}
                                                            onChange={this.handleChange}/>
                                                <Form.Input inverted placeholder='City'
                                                            name='city'
                                                            className="Info-Text"
                                                            value={this.state.city}
                                                            onChange={this.handleChange}/>
                                                <Form.Input inverted placeholder='Postal Code'
                                                            name='postalCode'
                                                            className="Info-Text"
                                                            value={this.state.postalCode}
                                                            onChange={this.handleChange}/>
                                                <Form.Input inverted placeholder='Country'
                                                            name='country'
                                                            className="Info-Text"
                                                            value={this.state.country}
                                                            onChange={this.handleChange}/>
                                            </Grid.Column>
                                        </Grid.Row>
                                    </Grid.Column>
                                </Grid.Row>
                            </Grid>

                        </Modal.Content>
                        <Modal.Actions>
                            <button className="Account-Button" onClick={this.addUser}>
                                <span className="Account-Text-Button">JOIN THE CREW</span>
                            </button>
                        </Modal.Actions>

                    </Modal>
                </div>
            </Aux>
        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.users.loading
    }
};

const dispatchToProps = dispatch => {
    return {
        addUser: (userToAdd) => dispatch(actions.saveUser(userToAdd))
    }
};

export default connect(mapStateToProps, dispatchToProps)(Account);