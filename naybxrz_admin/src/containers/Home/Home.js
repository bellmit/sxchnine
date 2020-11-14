import React,{Component} from 'react';

class Home extends Component {

    componentDidMount() {
        console.log(this.props)
    }

    render() {
        return (
            <div>Home</div>
        )
    };
}

export default Home;