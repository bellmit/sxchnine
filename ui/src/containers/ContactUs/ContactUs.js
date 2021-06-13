import React, {PureComponent} from 'react';
import {connect} from 'react-redux';
import {Container, Dimmer, Form, Grid, Header, Loader, TextArea} from 'semantic-ui-react';
import * as actionTypes from '../../store/actions';
import contact_back from './contact_back.jpg';
import './ContactUs.css';

class ContactUs extends PureComponent {

    state = {
        fullName: '',
        email: '',
        phone: '',
        message: '',
        loading: false,
        errorEmail:'',
        errorEmailFlag: false,
        errorFullName: '',
        errorFullNameFlag: false,
        errorMessage: '',
        errorMessageFlag: false
    }

    handleChange = (e, {name, value}) => this.setState({[name]: value});

    contactUs = () => {
        if (this.state.email === ''){
            this.setState({
                errorEmailFlag: true,
                errorEmail: 'Email is missing'
            })
        }

        if (this.state.fullName === ''){
            this.setState({
                errorFullNameFlag: true,
                errorFullName: 'Name is missing'
            })
        }

        if (this.state.message === ''){
            this.setState({
                errorMessageFlag: true,
                errorMessage: 'Message is missing'
            })
        }

        if (this.state.email !== ''
            && this.state.fullName !== ''
            && this.state.message !== ''){
            this.props.contact(this.createContact());
            this.props.history.push('/contactSent');
        }
    };

    createContact() {
        return {
            fullName: this.state.fullName,
            email: this.state.email,
            phoneNumber: this.state.phone,
            message: this.state.message
        }
    }

    render() {
        return (
            <div>
                <img src={contact_back}
                     alt="naybxrz econcept store vintage clothes 90's adidas carhartt obey supreme nike nocta bombers wutang chimodu hip hop culture streetwear lifestyle hoodies shirts hat bob"
                     className="ContactUs-Image-div"/>

                <Dimmer active={this.state.loading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                <div className="ContactUs-Form-Div">
                    <Container text className="ContactUs-Container-div">
                        <Header>
                            <span className="ContactUs-Message-Text-h1-div">Tell us everything !</span>
                        </Header>

                        <div className="ContactUs-Grid-div">
                            <Grid inverted>
                                <Grid.Row>
                                    <Form>
                                    <Form.Input required inverted
                                                size="mini"
                                                placeholder='Full Name ...'
                                                className="ContactUs-Info-Text"
                                                name='fullName'
                                                value={this.state.fullName}
                                                error={this.state.errorFullNameFlag && this.state.errorFullName}
                                                onChange={this.handleChange}/>
                                    </Form>
                                </Grid.Row>
                                <Grid.Row>
                                <Form>
                                    <Form.Input inverted required size="mini"
                                                placeholder='Email ...'
                                                className="ContactUs-Info-Text"
                                                name='email'
                                                value={this.state.email}
                                                error={this.state.errorEmailFlag && this.state.errorEmail}
                                                onChange={this.handleChange}/>
                                </Form>
                                </Grid.Row>
                                <Grid.Row>
                                <Form>
                                    <Form.Input inverted required size="mini"
                                                placeholder='Phone ...'
                                                className="ContactUs-Info-Text"
                                                name='phone'
                                                value={this.state.phone}
                                                onChange={this.handleChange}/>
                                </Form>
                                </Grid.Row>

                                <Grid.Row>
                                    <Form style={{width: '50%'}}>
                                        <TextArea placeholder='Tell us more'
                                                  style={{fontFamily: "American Typewriter", height: '150%'}}
                                                  name='message'
                                                  value={this.state.message}
                                                  error={this.state.errorMessageFlag && this.state.errorMessage}
                                                  onChange={this.handleChange}/>
                                    </Form>
                                </Grid.Row>
                                <Grid.Row>
                                    <button className="ContactUs-Continue-Button" onClick={this.contactUs}>
                                        <span className="ContactUs-Text-Button">SEND -></span>
                                    </button>
                                </Grid.Row>
                            </Grid>
                        </div>
                    </Container>
                </div>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.contact.loading
    }
};

const dispatchToProps = dispatch => {
    return {
        contact: (contact) => dispatch(actionTypes.contact(contact))
    }
};

export default connect(mapStateToProps, dispatchToProps)(ContactUs);