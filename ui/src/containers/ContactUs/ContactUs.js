import React, {PureComponent} from 'react';
import {connect} from 'react-redux';
import {Container, Header, Grid, Form, TextArea, Icon, Dimmer, Loader} from 'semantic-ui-react';
import * as actionTypes from '../../store/actions';
import contact_back from './contact_back.jpg';
import './ContactUs.css';

class ContactUs extends PureComponent {

    state = {
        fullName: '',
        email: '',
        phone: '',
        message: '',
        loading: false
    }

    componentDidMount() {
        console.log(this.props);
    }

    handleChange = (e, {name, value}) => this.setState({[name]: value});

    contactUs = () => {
        this.props.contact(this.createContact());
        this.props.history.push('/contactSent');
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
                <img src={contact_back} className="ContactUs-Image-div"/>

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
                                    <Form.Input required inverted
                                                style={{fontFamily: "American Typewriter, Times", fontSize: '60%'}}
                                                size="mini"
                                                placeholder='Full Name ...'
                                                className="Info-Text"
                                                name='fullName'
                                                value={this.state.fullName}
                                                onChange={this.handleChange}/>
                                </Grid.Row>
                                <Grid.Row>
                                    <Form.Input inverted required size="mini"
                                                style={{fontFamily: "American Typewriter", fontSize: '60%'}}
                                                placeholder='Email ...'
                                                className="Info-Text"
                                                name='email'
                                                value={this.state.email}
                                                onChange={this.handleChange}/>
                                </Grid.Row>
                                <Grid.Row>
                                    <Form.Input inverted required size="mini"
                                                style={{fontFamily: "American Typewriter", fontSize: '60%'}}
                                                placeholder='Phone ...'
                                                className="Info-Text"
                                                name='phone'
                                                value={this.state.phone}
                                                onChange={this.handleChange}/>
                                </Grid.Row>

                                <Grid.Row>
                                    <Form style={{width: '50%'}}>
                                        <TextArea placeholder='Tell us more'
                                                  style={{fontFamily: "American Typewriter", height: '150%'}}
                                                  name='message'
                                                  value={this.state.message}
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

                <div className="ContactUs-Foot-Div">
                    <Icon name="facebook" size='huge' color="blue"/>
                    <Icon name="instagram" size='huge' color="teal"/>
                    <Icon name="pinterest" size='huge' color="red"/>
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