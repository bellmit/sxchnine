import React, {Component} from "react";
import ReactImageMagnify from 'react-image-magnify';
import ReactSlick from './ShoppingSlick';



class Shopping extends Component {
    render(){
        return (
            <div>
                <div >
                    <ReactSlick />
                </div>
            </div>
        );
    }

}

export default Shopping;