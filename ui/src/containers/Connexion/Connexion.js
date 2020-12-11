import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Dimmer, Form, Label, Loader, Popup} from "semantic-ui-react";
import './Connexion.css';
import Aux from '../../hoc/Aux/Aux';
import user from './user.png';
import * as actions from "../../store/actions";


class Connexion extends Component {

    state = {
        open: false,
        email: '',
        password: ''
    }

    show = (size) => () => this.setState({size, open: true});
    close = () => this.setState({open: false});
    handleChange = (e, {name, value}) => this.setState({[name]: value});

    login = () => {
        // false = redirect to userAccount
        this.props.loginUser(this.state.email, this.state.password, this.props.history, false);
        this.props.fetchOrdersHistory(this.state.email);

    };

    checkIfAuthenticatedAndRedirect = () => {
        if (this.props.userAuthenticated !== ''){
            this.props.fetchOrdersHistory(this.props.userAuthenticated.email);
            this.props.history.push('/userAccount');

        }
    }


    render() {
        return (
            <Aux>
                <Dimmer active={this.props.loading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                <div>

                    <Popup pinned on='click'
                           position="bottom center"
                           trigger={ <img src={user}
                                          alt="connexion"
                                          onClick={this.checkIfAuthenticatedAndRedirect}
                                          className="Connexion-Button"/>}>

                    <Popup.Content>
                        <Form className="Connexion-div">
                            <Form.Field inline>
                                <p className="Connexion-Email-Text">EMAIL:</p>
                                <Form.Input size="small"
                                            inverted
                                            name='email'
                                            placeholder='Email'
                                            className="Connexion-Email-Text"
                                            value={this.state.email}
                                            onChange={this.handleChange}/>
                            </Form.Field>
                            <Form.Field inline>
                                <p className="Connexion-Email-Text">PASSWORD:</p>
                                <Form.Input size="small"
                                            inverted
                                            name='password'
                                            placeholder='Password'
                                            type='password'
                                            className="Connexion-Email-Text"
                                            value={this.state.password}
                                            onChange={this.handleChange} />
                                {this.props.error !== '' &&
                                <Label color="red">{this.props.error}</Label>}
                            </Form.Field>
                            <button className="Connexion-Continue-Button" onClick={this.login}>
                                <span className="Connexion-Text-Button">SIGN IN</span>
                            </button>
                        </Form>
                    </Popup.Content>
                    </Popup>
                </div>
            </Aux>
        );
    }
}

const mapStateToProps = state => {
    return {
        userAuth: state.users.userAuth,
        userAuthenticated: state.users.userAuthenticated,
        status: state.users.status,
        loading: state.users.loading,
        error: state.users.error
    }
};

const dispatchProps = dispatch => {
    return {
        loginUser: (email, password, history, order) => dispatch(actions.loginUser(email, password, history, order)),
        fetchOrdersHistory: (email) => dispatch(actions.fetchOrdersHistory(email))
    }
}

export default connect(mapStateToProps, dispatchProps)(Connexion);