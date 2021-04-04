import React, {Component} from 'react';
import {Button, Dimmer, Form, Grid, Label, Loader} from "semantic-ui-react";
import {connect} from 'react-redux';
import * as actions from '../../store/actions/';
import './Login.css';
import bg from './g-floyd-bg.jpg';


class Login extends Component {

    state = {
        email: '',
        password: '',
        error: '',
        emailMissingFlag: false,
        passwordMissingFlag: false
    }

    componentDidMount() {
        this.props.authenticate();
    }

    handleChange = (e, {name, value}) => this.setState({[name]: value});

    authenticateUser = () => {
        if (this.state.email !== '' && this.state.password !== '') {
            this.props.login(this.state.email, this.state.password, this.props.history);
            this.props.ordersByMonth();
            this.props.getOrdersNumber();
        }

        if (this.state.email === ''){
            this.setState({emailMissingFlag: true})
        }

        if (this.state.password === ''){
            this.setState({passwordMissingFlag: true})
        }

    }

    render() {

        let error = undefined
        if (this.props.userFail){
            error = <Label color='red' size='tiny' className="Label-Text">Unknown user. Check you credentials again...</Label>
        } else if (this.props.userError !== ''){
            error = <Label color='red' size='tiny' className="Label-Text">Network Error - Please try later</Label>
        }

        return (
            <div>
                <img alt="login" className="Login-Image-div" src={bg}/>
                <Dimmer active={this.props.loading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                <div className="Login-Form-Div">
                    <Grid inverted centered>
                        {error}
                        <Grid.Row>
                            <Form.Input required inverted
                                        size="mini"
                                        placeholder='Email..'
                                        className="Info-Text"
                                        name='email'
                                        error={this.state.emailMissingFlag && 'Missing email' }
                                        value={this.state.email}
                                        onChange={this.handleChange}/>
                        </Grid.Row>
                        <Grid.Row>
                            <Form.Input inverted required size="mini"
                                        placeholder='Password...'
                                        type='password'
                                        name='password'
                                        error={this.state.passwordMissingFlag && 'Missing password' }
                                        value={this.state.password}
                                        onChange={this.handleChange}/>
                        </Grid.Row>
                        <Grid.Row>
                            <Button className="Login-Button" color='black' onClick={this.authenticateUser}>
                                <span className="Login-Text-Button">Enter</span>
                            </Button>
                        </Grid.Row>
                    </Grid>
                </div>

            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.user.loading,
        userError: state.user.userError,
        userFail: state.user.userFail
    }
};

const dispatchToProps = dispatch => {
    return {
        authenticate: () => dispatch(actions.authenticate()),
        login: (email, password, history) => dispatch(actions.login(email, password, history)),
        ordersByMonth: () => dispatch(actions.ordersByMonth()),
        getOrdersNumber: () => dispatch(actions.getOrdersNumber())
    }
}

export default connect(mapStateToProps, dispatchToProps)(Login);