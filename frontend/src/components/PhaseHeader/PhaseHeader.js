import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import TextField from 'material-ui/TextField';
import { changeWip } from '../../actions';

class PhaseHeader extends React.Component {

  constructor({ id, name, wipLimit, style }) {
    super();
    this.state = {value: wipLimit};
    this.id = id;
    this.style = style;
    this.name = name;
    this.wipLimit = wipLimit;

    this.handleChange = this.handleChange.bind(this);
  };

  handleChange(event) {
    this.setState({value: event.target.value});
    const newWipLimit = event.target.value;
    if (newWipLimit !== '')
      this.props.dispatch(changeWip(this.id, event.target.value));
  }

  render() {
    const inputStyles = {
      mainElem: {
        width: 50,
        height: 35,
        marginLeft: 20,
        border: '2px dotted'
      },
      textField: {
        textAlign: 'center',
        fontWeight: 700,
        fontSize: '1.3em'
      },
      hint: {
        bottom: 3,
        left: 8
      }
    };
    return <div className="phase-header" style={this.style}>
      <div className="align-wrapper">
        <div className="phase-name">{this.name}</div>
        { !this.wipLimit && <div className="wip-limit">No WIP limit</div> }
        { this.wipLimit &&
        <form>
          <label>WIP Limit</label>
          <TextField
            hintText="WIP"
            value={this.state.value} onChange={this.handleChange}
            style={inputStyles.mainElem}
            inputStyle={inputStyles.textField}
            hintStyle={inputStyles.hint}
          />
        </form>
        }
      </div>
    </div>
  }

}

PhaseHeader.propTypes = {
  name: PropTypes.string.isRequired,
  wipLimit: PropTypes.number,
  styles: PropTypes.object
};

export default connect()(PhaseHeader);
